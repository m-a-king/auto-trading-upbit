package com.making.auto_trading_with_upbit_api.client.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum UpbitApiPath {

    // Market
    MARKET_ALL("/v1/market/all", HttpMethod.GET, false),
    TICKER("/v1/ticker", HttpMethod.GET, false),
    TRADES("/v1/trades", HttpMethod.GET, false),
    ORDERBOOK("/v1/orderbook", HttpMethod.GET, false),

    // Candles
    CANDLES_MINUTES("/v1/candles/minutes", HttpMethod.GET, false),
    CANDLES_DAYS("/v1/candles/days", HttpMethod.GET, false),
    CANDLES_WEEKS("/v1/candles/weeks", HttpMethod.GET, false),
    CANDLES_MONTHS("/v1/candles/months", HttpMethod.GET, false),

    // Accounts
    ACCOUNTS("/v1/accounts", HttpMethod.GET, true),

    // Orders
    ORDERS("/v1/orders", HttpMethod.GET, true),
    ORDERS_OPEN("/v1/orders/open", HttpMethod.GET, true);

    private final String path;
    private final HttpMethod method;
    private final boolean isPrivate;

    public String withUnit(final Integer unit) {
        // TODO: unit 명확해진 후, 관리
        if (unit == null) {
            return path;
        }
        return path + "/" + unit;
    }
}
