package com.making.auto_trading_with_upbit_api.client.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum OrderUpbitApiPath implements UpbitApiPath {

    GET_ORDER("v1/order", HttpMethod.GET),
    GET_ORDERS("/v1/orders", HttpMethod.GET),
    GET_OPEN_ORDERS("/v1/orders/open", HttpMethod.GET),
    GET_CLOSED_ORDERS("/v1/orders/closed", HttpMethod.GET),

    CREATE_ORDERS("/v1/orders", HttpMethod.POST),
    CANCEL_AND_CREATE_ORDERS("v1/orders/cancel_and_new", HttpMethod.POST),

    CANCEL_ORDER("v1/order", HttpMethod.DELETE),
    CANCEL_ORDERS("v1/orders", HttpMethod.DELETE),
    CANCEL_OPEN_ORDERS("v1/orders/open", HttpMethod.DELETE),

    CREATE_TEST_ORDERS("/v1/orders/test", HttpMethod.POST),
    ;

    private final String path;
    private final HttpMethod method;

    @Override
    public boolean isPrivate() {
        return true;
    }
}
