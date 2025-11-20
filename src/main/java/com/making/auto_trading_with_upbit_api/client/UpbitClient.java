package com.making.auto_trading_with_upbit_api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.making.auto_trading_with_upbit_api.client.auth.JwtCreator;
import com.making.auto_trading_with_upbit_api.client.config.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.constants.ApiConstants;
import com.making.auto_trading_with_upbit_api.dto.UpbitApiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpbitClient {

    private final RestClient restClientForUpbit;
    private final ObjectMapper objectMapper;
    private final JwtCreator jwtCreator;

    public UpbitApiResponse requestGet(
            final UpbitApiPath path,
            final Integer unit,
            final Map<String, List<String>> params
    ) {
        final RequestHeadersSpec<?> requestSpec = restClientForUpbit.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path.withUnit(unit));
                    if (params.isEmpty()) {
                        return uriBuilder.build();
                    }
                    params.forEach((key, values) -> values.forEach(value -> uriBuilder.queryParam(key, value)));
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON);

        authenticateIfNeeded(path, params, requestSpec);

        return parseResponse(requestSpec.retrieve().toEntity(String.class));
    }

    public UpbitApiResponse requestGet(
            final UpbitApiPath path,
            final Map<String, List<String>> params
    ) {
        return requestGet(path, null, params);
    }

    public UpbitApiResponse requestGet(
            final UpbitApiPath path
    ) {
        return requestGet(path, null, Map.of());
    }

    private void authenticateIfNeeded(
            final UpbitApiPath path,
            final Map<String, List<String>> params,
            final RequestHeadersSpec<?> requestSpec
    ) {
        if (path.isPrivate()) {
            final String queryString = buildQueryString(params);
            jwtCreator.create(queryString);
            requestSpec.header(HttpHeaders.AUTHORIZATION, jwtCreator.create(queryString).WithPrefix());
        }
    }

    private String buildQueryString(final Map<String, List<String>> params) {
        if (params == null || params.isEmpty()) {
            return ApiConstants.EMPTY_STRING;
        }

        final StringBuilder queryString = new StringBuilder();
        params.forEach((key, values) -> {
            for (final String value : values) {
                if (!queryString.isEmpty()) {
                    queryString.append("&");
                }

                if (values.size() > 1) {
                    queryString.append(key).append("[]=").append(value);
                } else {
                    queryString.append(key).append("=").append(value);
                }
            }
        });

        return queryString.toString();
    }

    private UpbitApiResponse parseResponse(final ResponseEntity<String> responseEntity) {
        final HttpStatusCode statusCode = responseEntity.getStatusCode();
        final String body = responseEntity.getBody();

        if (statusCode.is2xxSuccessful()) {
            try {
                final JsonNode data = objectMapper.readTree(body);
                return UpbitApiResponse.success(data);
            } catch (final Exception e) {
                log.error("Failed to parse response body", e);
                return UpbitApiResponse.error(statusCode, ApiConstants.PARSE_ERROR, e.getMessage());
            }
        }

        return parseErrorResponse(statusCode, body);
    }

    private UpbitApiResponse parseErrorResponse(final HttpStatusCode statusCode, final String body) {
        try {
            final JsonNode root = objectMapper.readTree(body);
            if (root.has(ApiConstants.ERROR)) {
                final JsonNode error = root.get(ApiConstants.ERROR);
                final String name =
                        error.has(ApiConstants.NAME) ? error.get(ApiConstants.NAME).asText()
                                : ApiConstants.EMPTY_STRING;
                final String message =
                        error.has(ApiConstants.MESSAGE) ? error.get(ApiConstants.MESSAGE).asText()
                                : ApiConstants.EMPTY_STRING;
                return UpbitApiResponse.error(statusCode, name, message);
            }
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY_STRING, root.toString());
        } catch (final Exception e) {
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY_STRING, body);
        }
    }
}
