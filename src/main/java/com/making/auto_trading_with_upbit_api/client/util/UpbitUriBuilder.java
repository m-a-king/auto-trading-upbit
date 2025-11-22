package com.making.auto_trading_with_upbit_api.client.util;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.making.auto_trading_with_upbit_api.client.dto.UpbitRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class UpbitUriBuilder {

    public URI build(final UpbitRequest request) {
        if (shouldUseQueryParams(request)) {
            return buildUriWithQueryParams(request);
        }
        if (shouldUseRequestBody(request)) {
            return buildUriWithoutQueryParams(request);
        }
        throw new IllegalArgumentException("Unsupported HTTP method: " + request.method());
    }

    private URI buildUriWithQueryParams(final UpbitRequest request) {
        return UriComponentsBuilder.newInstance()
                .path(request.resolvedPath())
                .queryParams(request.requestParams())
                .build()
                .toUri();
    }

    private URI buildUriWithoutQueryParams(final UpbitRequest request) {
        return UriComponentsBuilder.newInstance()
                .path(request.resolvedPath())
                .build()
                .toUri();
    }

    private boolean shouldUseRequestBody(final UpbitRequest request) {
        return request.method() == POST;
    }

    private boolean shouldUseQueryParams(final UpbitRequest request) {
        return request.method() == GET || request.method() == DELETE;
    }
}
