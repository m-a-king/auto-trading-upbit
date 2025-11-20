package com.making.auto_trading_with_upbit_api.client;

import com.making.auto_trading_with_upbit_api.client.auth.JwtCreator;
import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.util.UpbitResponseParser;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

import static com.making.auto_trading_with_upbit_api.client.util.QueryStringBuilder.*;

@RequiredArgsConstructor
@Component
public class UpbitClient {

    private final RestClient restClientForUpbit;
    private final JwtCreator jwtCreator;
    private final UpbitResponseParser responseParser;

    public UpbitApiResponse requestGet(
            final UpbitApiPath path,
            final Integer unit,
            final MultiValueMap<String, String> params
    ) {
        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path.withUnit(unit));
                    uriBuilder.queryParams(params);
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON);

        authenticateIfNeeded(path, params, requestSpec);

        return responseParser.parse(requestSpec.retrieve().toEntity(String.class));
    }

    public UpbitApiResponse requestGet(
            final UpbitApiPath path,
            final MultiValueMap<String, String> params
    ) {
        return requestGet(path, null, params);
    }

    public UpbitApiResponse requestGet(
            final UpbitApiPath path
    ) {
        return requestGet(path, null, new LinkedMultiValueMap<>());
    }

    private void authenticateIfNeeded(
            final UpbitApiPath path,
            final MultiValueMap<String, String> params,
            final RequestHeadersSpec<?> requestSpec
    ) {
        if (path.isPrivate()) {
            final String queryString = build(params);
            requestSpec.header(HttpHeaders.AUTHORIZATION, jwtCreator.create(queryString).WithPrefix());
        }
    }
}
