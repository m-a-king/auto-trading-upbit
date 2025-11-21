package com.making.auto_trading_with_upbit_api.client.path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum CommonUpbitApiPath implements UpbitApiPath {

    // Market
    MARKET_ALL("/v1/market/all", HttpMethod.GET, false),
    TICKER("/v1/ticker", HttpMethod.GET, false),
    TRADES("/v1/trades", HttpMethod.GET, false),
    ORDERBOOK("/v1/orderbook", HttpMethod.GET, false),

    // Accounts
    ACCOUNTS("/v1/accounts", HttpMethod.GET, true),
    ;

    private final String value;
    private final HttpMethod method;
    private final boolean isPrivate;
}
