package com.making.auto_trading_with_upbit_api.client.dto;

import com.making.auto_trading_with_upbit_api.client.path.PathParam;
import com.making.auto_trading_with_upbit_api.client.path.UpbitApiPath;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 업비트 API 요청 정보를 담는 DTO.
 *
 * @param path          API 엔드포인트 (HTTP 메서드, 경로, 인증 여부 포함)
 * @param pathParam     URL 경로의 {placeholder} 치환 값 (예: /candles/{unit}/{minute} → /candles/minutes/1)
 * @param requestParams GET/DELETE → 쿼리 파라미터, POST → 요청 바디로 사용
 */
public record UpbitRequest(
        UpbitApiPath path,
        PathParam pathParam,
        MultiValueMap<String, String> requestParams
) {
    public static UpbitRequest of(final UpbitApiPath path) {
        return new UpbitRequest(path, null, new LinkedMultiValueMap<>());
    }

    public static UpbitRequest of(
            final UpbitApiPath path,
            final MultiValueMap<String, String> params
    ) {
        return new UpbitRequest(path, null, params);
    }

    public static UpbitRequest of(
            final UpbitApiPath path,
            final PathParam pathParam,
            final MultiValueMap<String, String> params
    ) {
        return new UpbitRequest(path, pathParam, params);
    }

    public HttpMethod method() {
        return path.getMethod();
    }

    public boolean isPrivate() {
        return path.isPrivate();
    }

    public String resolvedPath() {
        return path.getValue(pathParam);
    }

    public Map<String, String> body() {
        validateSingleValueMap(requestParams);
        return requestParams.toSingleValueMap();
    }

    private void validateSingleValueMap(final MultiValueMap<String, String> multiValueMap) {
        multiValueMap.forEach((key, values) -> {
            if (values.size() > 1) {
                throw new IllegalArgumentException(
                        "POST request body must have single value per key. "
                                + "Key '" + key + "' has " + values.size() + " values"
                );
            }
        });
    }
}
