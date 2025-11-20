package com.making.auto_trading_with_upbit_api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 업비트 현재가(Ticker) 정보
 *
 * @param market             마켓 코드 (KRW-BTC)
 * @param tradeDate          최근 거래 일자 (UTC)
 * @param tradeTime          최근 거래 시각 (UTC)
 * @param tradeDateKst       최근 거래 일자 (KST)
 * @param tradeTimeKst       최근 거래 시각 (KST)
 * @param tradeTimestamp     체결 타임스탬프 (milliseconds)
 * @param openingPrice       시가
 * @param highPrice          고가
 * @param lowPrice           저가
 * @param tradePrice         현재가
 * @param prevClosingPrice   전일 종가
 * @param change             전일 대비 (RISE, EVEN, FALL)
 * @param changePrice        부호 없는 전일 대비 값
 * @param changeRate         부호 없는 전일 대비 등락율
 * @param signedChangePrice  전일 대비 값 (부호 있음)
 * @param signedChangeRate   전일 대비 등락율 (부호 있음)
 * @param tradeVolume        가장 최근 거래량
 * @param accTradePrice      누적 거래대금 (UTC 0시 기준)
 * @param accTradePrice24h   24시간 누적 거래대금
 * @param accTradeVolume     누적 거래량 (UTC 0시 기준)
 * @param accTradeVolume24h  24시간 누적 거래량
 * @param highest52WeekPrice 52주 최고가
 * @param highest52WeekDate  52주 최고가 달성일
 * @param lowest52WeekPrice  52주 최저가
 * @param lowest52WeekDate   52주 최저가 달성일
 * @param timestamp          타임스탬프 (milliseconds)
 */
public record Ticker(
        String market,
        String tradeDate,
        String tradeTime,
        String tradeDateKst,
        String tradeTimeKst,
        Long tradeTimestamp,
        BigDecimal openingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal tradePrice,
        BigDecimal prevClosingPrice,
        String change,
        BigDecimal changePrice,
        Double changeRate,
        BigDecimal signedChangePrice,
        Double signedChangeRate,
        BigDecimal tradeVolume,
        BigDecimal accTradePrice,
        @JsonProperty("acc_trade_price_24h") BigDecimal accTradePrice24h,
        BigDecimal accTradeVolume,
        @JsonProperty("acc_trade_volume_24h") BigDecimal accTradeVolume24h,
        @JsonProperty("highest_52_week_price") BigDecimal highest52WeekPrice,
        @JsonProperty("highest_52_week_date") String highest52WeekDate,
        @JsonProperty("lowest_52_week_price") BigDecimal lowest52WeekPrice,
        @JsonProperty("lowest_52_week_date") String lowest52WeekDate,
        Long timestamp
) {
    public Double changeRatePercent() {
        return changeRate != null ? changeRate * 100 : 0.0;
    }

    public boolean isRising() {
        return changeRate != null && changeRate > 0;
    }

    public boolean isFalling() {
        return changeRate != null && changeRate < 0;
    }
}
