package com.making.auto_trading_with_upbit_api.client.constants;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ApiConstants {

    // Error Codes
    public static final String PARSE_ERROR = "PARSE_ERROR";

    // JSON Fields & API Parameters
    public static final String ERROR = "error";
    public static final String NAME = "name";
    public static final String MESSAGE = "message";

    // Empty Values
    public static final String EMPTY_STRING = "";
    public static final Map<Object, Object> EMPTY_MAP = Map.of();
}
