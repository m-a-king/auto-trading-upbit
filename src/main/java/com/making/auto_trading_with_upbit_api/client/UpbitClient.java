package com.making.auto_trading_with_upbit_api.client;

import com.making.auto_trading_with_upbit_api.client.auth.JwtCreator;
import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.util.QueryStringBuilder;
import com.making.auto_trading_with_upbit_api.client.util.UpbitResponseParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

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

    public UpbitApiResponse requestPost(
            final UpbitApiPath path,
            final MultiValueMap<String, String> params
    ) {
        validateSingleValueMap(params);

        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.post()
                .uri(path.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(params.toSingleValueMap());

        authenticateIfNeeded(path, params, requestSpec);

        return responseParser.parse(requestSpec.retrieve().toEntity(String.class));
    }

    private void authenticateIfNeeded(
            final UpbitApiPath path,
            final MultiValueMap<String, String> params,
            final RequestHeadersSpec<?> requestSpec
    ) {
        if (path.isPrivate()) {
            final String queryString = QueryStringBuilder.build(params);
            requestSpec.header(HttpHeaders.AUTHORIZATION, jwtCreator.create(queryString).WithPrefix());
        }
    }

    private void validateSingleValueMap(final MultiValueMap<String, String> params) {
        params.forEach((key, values) -> {
            if (values.size() > 1) {
                throw new IllegalArgumentException(
                        "POST request body must have single value per key. "
                                + "Key '" + key + "' has " + values.size() + " values"
                );
            }
        });
    }
}
