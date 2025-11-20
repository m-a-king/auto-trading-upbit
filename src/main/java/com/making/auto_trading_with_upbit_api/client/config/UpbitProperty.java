package com.making.auto_trading_with_upbit_api.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Upbit API 설정
 */
@ConfigurationProperties(prefix = "upbit.api")
public record UpbitProperty(
        String baseUrl,
        String accessKey,
        String secretKey
) {
}
