package com.making.auto_trading_with_upbit_api.client.constants;

import org.springframework.http.HttpMethod;

public interface UpbitApiPath {

    String getPath();
    HttpMethod getMethod();
    boolean isPrivate();
}
