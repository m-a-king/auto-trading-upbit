package com.making.auto_trading_with_upbit_api.client.path;

import com.making.auto_trading_with_upbit_api.client.constants.ApiConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public enum CandleUpbitApiPath implements UpbitApiPath {

    CANDLES("/v1/candles", HttpMethod.GET, false);

    private final String value;

    @Getter
    private final HttpMethod method;

    @Getter
    private final boolean isPrivate;

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Candle 관련 API는 Path에 unit이 필요합니다.");
    }

    @Override
    public String getValue(final PathParam unit) {
        return value + unit.getValue();
    }

    @Getter
    @RequiredArgsConstructor
    public enum CandleUnit {
        SECOND("/seconds", false),
        MINUTE("/minutes", true),
        DAY("/days", false),
        WEEK("/weeks", false),
        MONTH("/months", false),
        YEAR("/years", false);

        private final String path;
        private final boolean needMoreUnit;
    }

    @RequiredArgsConstructor
    public enum CandleMinuteUnit {
        NULL(-1),
        ONE(1),
        THREE(3),
        FIVE(5),
        TEN(10),
        FIFTEEN(15),
        THIRTY(30),
        SIXTY(60),
        TWO_HUNDRED_FORTY(240);

        private final int value;

        public String getPath() {
            if (this == NULL) {
                return ApiConstants.EMPTY_STRING;
            }
            return "/" + value;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CandlePathParam implements PathParam {
        private final CandleUnit candleUnit;
        private final CandleMinuteUnit candleMinuteUnit;

        public static CandlePathParam of(
                final CandleUnit candleUnit,
                final CandleMinuteUnit candleMinuteUnit
        ) {
            return new CandlePathParam(candleUnit, candleMinuteUnit);
        }

        public static CandlePathParam from(final CandleUnit candleUnit) {
            return new CandlePathParam(candleUnit, CandleMinuteUnit.NULL);
        }

        @Override
        public String getValue() {
            return candleUnit.getPath() + candleMinuteUnit.getPath();
        }
    }
}
