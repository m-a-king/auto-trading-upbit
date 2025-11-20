package com.making.auto_trading_with_upbit_api.client.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.making.auto_trading_with_upbit_api.client.constants.ApiConstants;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpbitResponseParser {

    private final ObjectMapper objectMapper;

    public UpbitApiResponse parse(final ResponseEntity<String> responseEntity) {
        final HttpStatusCode statusCode = responseEntity.getStatusCode();
        final String body = responseEntity.getBody();

        if (statusCode.is2xxSuccessful()) {
            return parseSuccessResponse(statusCode, body);
        }

        return parseErrorResponse(statusCode, body);
    }

    private UpbitApiResponse parseSuccessResponse(final HttpStatusCode statusCode, final String body) {
        try {
            final JsonNode data = objectMapper.readTree(body);
            return UpbitApiResponse.success(data);
        } catch (final Exception e) {
            log.error("Failed to parse success response body", e);
            return UpbitApiResponse.error(statusCode, ApiConstants.PARSE_ERROR, e.getMessage());
        }
    }

    private UpbitApiResponse parseErrorResponse(final HttpStatusCode statusCode, final String body) {
        try {
            final JsonNode root = objectMapper.readTree(body);
            if (root.has(ApiConstants.ERROR)) {
                final JsonNode error = root.get(ApiConstants.ERROR);
                final String name = extractField(error, ApiConstants.NAME);
                final String message = extractField(error, ApiConstants.MESSAGE);
                return UpbitApiResponse.error(statusCode, name, message);
            }
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY_STRING, root.toString());
        } catch (final Exception e) {
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY_STRING, body);
        }
    }

    private String extractField(final JsonNode node, final String fieldName) {
        if (node.has(fieldName)) {
            return node.get(fieldName).asText();
        }
        return ApiConstants.EMPTY_STRING;
    }
}
