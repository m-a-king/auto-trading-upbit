package com.making.auto_trading_with_upbit_api.indicator.domain;

import java.math.BigDecimal;

/**
 * RSI (Relative Strength Index) 계산 결과
 *
 * @param value         RSI 값 (0-100)
 * @param au            누적 평균 상승폭
 * @param ad            누적 평균 하락폭
 * @param rs            상대 강도 (AU/AD)
 * @param period        계산에 사용된 기간
 * @param isOverbought  과매수 상태 여부 (RSI >= 70)
 * @param isOversold    과매도 상태 여부 (RSI <= 30)
 */
public record RsiResult(
        BigDecimal value,
        BigDecimal au,
        BigDecimal ad,
        BigDecimal rs,
        int period,
        boolean isOverbought,
        boolean isOversold
) {

    private static final BigDecimal OVERBOUGHT_THRESHOLD = new BigDecimal("70");
    private static final BigDecimal OVERSOLD_THRESHOLD = new BigDecimal("30");

    public static RsiResult of(
            final BigDecimal value,
            final BigDecimal au,
            final BigDecimal ad,
            final BigDecimal rs,
            final int period
    ) {
        final boolean isOverbought = value.compareTo(OVERBOUGHT_THRESHOLD) >= 0;
        final boolean isOversold = value.compareTo(OVERSOLD_THRESHOLD) <= 0;

        return new RsiResult(value, au, ad, rs, period, isOverbought, isOversold);
    }

    /**
     * RSI 상태를 문자열로 반환
     *
     * @return "과매수", "과매도", "중립"
     */
    public String getStatus() {
        if (isOverbought) {
            return "과매수";
        }
        if (isOversold) {
            return "과매도";
        }
        return "중립";
    }
}
