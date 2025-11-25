package com.making.auto_trading_with_upbit_api.client.path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum OrderUpbitApiPath implements UpbitApiPath {

    // 조회
    GET_ORDER_CHANCE("/v1/orders/chance", HttpMethod.GET),
    GET_ORDER("/v1/order", HttpMethod.GET),
    GET_ORDERS("/v1/orders", HttpMethod.GET),
    GET_OPEN_ORDERS("/v1/orders/open", HttpMethod.GET),
    GET_CLOSED_ORDERS("/v1/orders/closed", HttpMethod.GET),

    // 생성
    CREATE_ORDER("/v1/orders", HttpMethod.POST),
    CREATE_TEST_ORDER("/v1/orders/test", HttpMethod.POST),

    // 취소
    CANCEL_ORDER("/v1/order", HttpMethod.DELETE),
    CANCEL_ORDERS("/v1/orders", HttpMethod.DELETE),
    CANCEL_ALL_ORDERS("/v1/orders/open", HttpMethod.DELETE),

    // 취소 후 재주문
    CANCEL_AND_NEW_ORDER("/v1/orders/cancel_and_new", HttpMethod.POST),
    ;

    private final String value;
    private final HttpMethod method;

    @Override
    public boolean isPrivate() {
        return true;
    }
}
