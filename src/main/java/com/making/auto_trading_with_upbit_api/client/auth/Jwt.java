package com.making.auto_trading_with_upbit_api.client.auth;

public record Jwt(
        String value
) {

    public static final String BEARER_PREFIX = "Bearer ";

    public String WithPrefix() {
        return BEARER_PREFIX + value;
    }
}
