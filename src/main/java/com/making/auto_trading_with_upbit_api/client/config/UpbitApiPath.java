package com.making.auto_trading_with_upbit_api.client.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpbitApiPath {

    // Market (Public)
    MARKET_ALL("/v1/market/all"),
    TICKER("/v1/ticker"),
    TRADES("/v1/trades"),
    ORDERBOOK("/v1/orderbook"),

    // Candles (Public)
    CANDLES_MINUTES("/v1/candles/minutes"),
    CANDLES_DAYS("/v1/candles/days"),
    CANDLES_WEEKS("/v1/candles/weeks"),
    CANDLES_MONTHS("/v1/candles/months"),

    // Accounts (Private)
    ACCOUNTS("/v1/accounts"),

    // Orders (Private)
    ORDERS("/v1/orders"),
    ORDERS_OPEN("/v1/orders/open");

    private final String path;

    public String withUnit(final int unit) {
        return path + "/" + unit;
    }
}
