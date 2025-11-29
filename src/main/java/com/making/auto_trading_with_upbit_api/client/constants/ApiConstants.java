package com.making.auto_trading_with_upbit_api.client.constants;

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
    public static final String MARKET = "market";
    public static final String MARKETS = "markets";
    public static final String COUNT = "count";
    public static final String UUID = "uuid";
    public static final String UUIDS_ARRAY = "uuids[]";
    public static final String DAYS = "days";

    // API Limits
    public static final int MAX_CANDLE_COUNT = 200;

    // Empty Values
    public static final String EMPTY_STRING = "";
}
