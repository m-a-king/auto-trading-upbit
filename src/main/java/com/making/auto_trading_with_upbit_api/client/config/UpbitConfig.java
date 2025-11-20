package com.making.auto_trading_with_upbit_api.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UpbitProperty.class)
public class UpbitConfig {

    private final UpbitProperty upbitProperty;

    @Bean
    public RestClient restClientForUpbit() {
        return RestClient.create(upbitProperty.baseUrl());
    }

    @Bean
    public ObjectMapper objectMapperForUpbit() {
        final ObjectMapper mapper = new ObjectMapper();
        // snake_case JSON을 camelCase 필드로 자동 변환
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper;
    }
}
