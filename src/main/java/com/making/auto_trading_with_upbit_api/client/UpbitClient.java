package com.making.auto_trading_with_upbit_api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.making.auto_trading_with_upbit_api.constants.ApiConstants;
import com.making.auto_trading_with_upbit_api.dto.UpbitApiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpbitClient {

    private final RestClient restClientForUpbit;
    private final ObjectMapper objectMapper;

    public UpbitApiResponse requestGet(
            final String path,
            final Map<String, List<String>> params
    ) {
        final ResponseEntity<String> entity = restClientForUpbit.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (params.isEmpty()) {
                        return uriBuilder.build();
                    }
                    params.forEach((key, values) -> values.forEach(value -> uriBuilder.queryParam(key, value)));
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        log.debug(entity.toString());

        return parseResponse(entity);
    }

    public UpbitApiResponse requestGet(
            final String path
    ) {
        return requestGet(path, null);
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
                        error.has(ApiConstants.NAME) ? error.get(ApiConstants.NAME).asText() : ApiConstants.EMPTY;
                final String message =
                        error.has(ApiConstants.MESSAGE) ? error.get(ApiConstants.MESSAGE).asText() : ApiConstants.EMPTY;
                return UpbitApiResponse.error(statusCode, name, message);
            }
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY, root.toString());
        } catch (final Exception e) {
            return UpbitApiResponse.error(statusCode, ApiConstants.EMPTY, body);
        }
    }
}
