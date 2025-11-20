package com.making.auto_trading_with_upbit_api.service.dto;

import java.math.BigDecimal;

/**
 * 업비트 계정 잔고 정보
 *
 * @param currency           화폐를 의미하는 영문 대문자 코드 (KRW, BTC, ETH 등)
 * @param balance            주문가능 금액/수량
 * @param locked             주문 중 묶여있는 금액/수량
 * @param avgBuyPrice        매수평균가
 * @param avgBuyPriceModified 매수평균가 수정 여부
 * @param unitCurrency       평단가 기준 화폐 (KRW)
 */
public record Account(
        String currency,
        BigDecimal balance,
        BigDecimal locked,
        BigDecimal avgBuyPrice,
        boolean avgBuyPriceModified,
        String unitCurrency
) {
    /**
     * 총 보유량 (주문가능 + 주문중)
     */
    public BigDecimal totalBalance() {
        return balance.add(locked);
    }

    /**
     * KRW 계정 여부
     */
    public boolean isKrw() {
        return "KRW".equals(currency);
    }
}