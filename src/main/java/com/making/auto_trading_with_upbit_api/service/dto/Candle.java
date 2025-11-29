package com.making.auto_trading_with_upbit_api.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 * 업비트 캔들 정보
 *
 * @param market               마켓 코드 (KRW-BTC)
 * @param candleDateTimeUtc    캔들 기준 일시 (UTC)
 * @param candleDateTimeKst    캔들 기준 일시 (KST)
 * @param openingPrice         시가
 * @param highPrice            고가
 * @param lowPrice             저가
 * @param tradePrice           종가 (현재가)
 * @param timestamp            마지막 틱 저장 시각 (milliseconds)
 * @param candleAccTradePrice  누적 거래대금
 * @param candleAccTradeVolume 누적 거래량
 * @param unit                 분 단위 (분 캔들에만 존재, 1/3/5/10/15/30/60/240)
 * @param prevClosingPrice     전일 종가 (일 캔들에만 존재)
 * @param changePrice          전일 대비 값 (일 캔들에만 존재)
 * @param changeRate           전일 대비 등락율 (일 캔들에만 존재)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Candle(
        String market,
        String candleDateTimeUtc,
        String candleDateTimeKst,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        Long timestamp,
        BigDecimal candleAccTradePrice,
        BigDecimal candleAccTradeVolume,
        Integer unit,
        BigDecimal prevClosingPrice,
        BigDecimal changePrice,
        Double changeRate
) {
    /**
     * 등락율 퍼센트 변환
     */
    public Double changeRatePercent() {
        return changeRate != null ? changeRate * 100 : null;
    }

    /**
     * 캔들 변동폭 (고가 - 저가)
     */
    public BigDecimal priceRange() {
        if (highPrice == null || lowPrice == null) {
            return BigDecimal.ZERO;
        }
        return highPrice.subtract(lowPrice);
    }

    /**
     * 양봉 여부 (종가 > 시가)
     */
    public boolean isBullish() {
        if (tradePrice == null || openingPrice == null) {
            return false;
        }
        return tradePrice.compareTo(openingPrice) > 0;
    }

    /**
     * 음봉 여부 (종가 < 시가)
     */
    public boolean isBearish() {
        if (tradePrice == null || openingPrice == null) {
            return false;
        }
        return tradePrice.compareTo(openingPrice) < 0;
    }
}
