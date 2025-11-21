package com.making.auto_trading_with_upbit_api.client.path;

import org.springframework.http.HttpMethod;

public interface UpbitApiPath {

    String getValue();

    default String getValue(final PathParam param) {
        return getValue();
    }

    HttpMethod getMethod();

    boolean isPrivate();
}
