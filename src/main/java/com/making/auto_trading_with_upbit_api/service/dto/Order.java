package com.making.auto_trading_with_upbit_api.service.dto;

import java.math.BigDecimal;

/**
 * 업비트 주문 정보
 *
 * @param uuid            주문 고유 식별자
 * @param side            주문 방향 (bid: 매수, ask: 매도)
 * @param ordType         주문 유형 (limit: 지정가, price: 시장가매수, market: 시장가매도, best: 최유리지정가)
 * @param price           주문 단가 또는 총액
 * @param state           주문 상태 (wait: 체결대기, watch: 예약주문대기, done: 전체체결완료, cancel: 주문취소)
 * @param market          페어 코드 (KRW-BTC)
 * @param createdAt       주문 생성 시각 (KST)
 * @param volume          주문 수량
 * @param remainingVolume 미체결 수량
 * @param executedVolume  체결된 수량
 * @param reservedFee     예약된 수수료
 * @param remainingFee    남은 수수료
 * @param paidFee         사용된 수수료
 * @param locked          거래에 묶여있는 비용
 * @param tradesCount     체결 건수
 * @param timeInForce     주문 체결 조건 (ioc, fok, post_only)
 * @param identifier      사용자 지정 주문 ID
 * @param smpType         SMP 모드 (cancel_maker, cancel_taker, reduce)
 * @param preventedVolume SMP로 인해 취소된 수량
 * @param preventedLocked SMP로 인해 해제된 자산
 */
public record Order(
        String uuid,
        String side,
        String ordType,
        BigDecimal price,
        String state,
        String market,
        String createdAt,
        BigDecimal volume,
        BigDecimal remainingVolume,
        BigDecimal executedVolume,
        BigDecimal reservedFee,
        BigDecimal remainingFee,
        BigDecimal paidFee,
        BigDecimal locked,
        Integer tradesCount,
        String timeInForce,
        String identifier,
        String smpType,
        BigDecimal preventedVolume,
        BigDecimal preventedLocked
) {
    /**
     * 매수 주문 여부
     */
    public boolean isBuy() {
        return "bid".equals(side);
    }

    /**
     * 매도 주문 여부
     */
    public boolean isSell() {
        return "ask".equals(side);
    }

    /**
     * 주문 완료 여부
     */
    public boolean isDone() {
        return "done".equals(state);
    }

    /**
     * 주문 취소 여부
     */
    public boolean isCancelled() {
        return "cancel".equals(state);
    }
}
