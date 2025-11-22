package com.making.auto_trading_with_upbit_api.client;

import com.making.auto_trading_with_upbit_api.client.auth.JwtCreator;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitRequest;
import com.making.auto_trading_with_upbit_api.client.util.QueryStringBuilder;
import com.making.auto_trading_with_upbit_api.client.util.UpbitResponseParser;
import com.making.auto_trading_with_upbit_api.client.util.UpbitUriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

@RequiredArgsConstructor
@Component
public class UpbitClient {

    private final RestClient restClientForUpbit;
    private final JwtCreator jwtCreator;
    private final UpbitResponseParser responseParser;
    private final UpbitUriBuilder uriBuilder;

    public UpbitApiResponse requestGet(final UpbitRequest request) {
        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.get()
                .uri(uriBuilder.build(request))
                .accept(MediaType.APPLICATION_JSON);

        authenticateIfNeeded(request, requestSpec);

        return responseParser.parse(requestSpec.retrieve().toEntity(String.class));
    }

    public UpbitApiResponse requestPost(final UpbitRequest request) {
        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.post()
                .uri(uriBuilder.build(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request.body());

        authenticateIfNeeded(request, requestSpec);

        return responseParser.parse(requestSpec.retrieve().toEntity(String.class));
    }

    public UpbitApiResponse requestDelete(final UpbitRequest request) {
        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.delete()
                .uri(uriBuilder.build(request))
                .accept(MediaType.APPLICATION_JSON);

        authenticateIfNeeded(request, requestSpec);

        return responseParser.parse(requestSpec.retrieve().toEntity(String.class));
    }

    private void authenticateIfNeeded(
            final UpbitRequest request,
            final RequestHeadersSpec<?> requestSpec
    ) {
        if (request.isPrivate()) {
            final String queryString = QueryStringBuilder.build(request.requestParams());
            requestSpec.header(HttpHeaders.AUTHORIZATION, jwtCreator.create(queryString).WithPrefix());
        }
    }
}
