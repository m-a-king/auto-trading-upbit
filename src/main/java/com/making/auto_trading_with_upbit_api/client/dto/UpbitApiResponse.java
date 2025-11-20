package com.making.auto_trading_with_upbit_api.client.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Builder
public record UpbitApiResponse(
        boolean success,
        HttpStatusCode statusCode,
        JsonNode data,
        String errorName,
        String errorMessage
) {

    public static UpbitApiResponse success(final JsonNode data) {
        return UpbitApiResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK)
                .data(data)
                .build();
    }

    public static UpbitApiResponse error(
            final HttpStatusCode statusCode,
            final String errorName,
            final String errorMessage
    ) {
        return UpbitApiResponse.builder()
                .success(false)
                .statusCode(statusCode)
                .errorName(errorName)
                .errorMessage(errorMessage)
                .build();
    }
}
