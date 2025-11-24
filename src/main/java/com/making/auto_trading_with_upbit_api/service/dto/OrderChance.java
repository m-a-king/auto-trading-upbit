package com.making.auto_trading_with_upbit_api.service.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 페어별 주문 가능 정보
 *
 * @param bidFee      매수 수수료율
 * @param askFee      매도 수수료율
 * @param makerBidFee 메이커 매수 수수료율
 * @param makerAskFee 메이커 매도 수수료율
 * @param market      마켓 정보
 * @param bidAccount  매수 시 사용되는 계정 (KRW 등)
 * @param askAccount  매도 시 사용되는 계정 (BTC 등)
 */
public record OrderChance(
        BigDecimal bidFee,
        BigDecimal askFee,
        BigDecimal makerBidFee,
        BigDecimal makerAskFee,
        Market market,
        Account bidAccount,
        Account askAccount
) {
    /**
     * 마켓 정보
     *
     * @param id         마켓 코드 (예: KRW-BTC)
     * @param name       마켓 이름 (예: BTC/KRW)
     * @param orderTypes 지원 주문 유형 (deprecated, bid_types/ask_types 사용 권장)
     * @param orderSides 지원 주문 방향 (ask, bid)
     * @param bidTypes   매수 주문 유형 (limit, price, best_fok 등)
     * @param askTypes   매도 주문 유형 (limit, market, best_fok 등)
     * @param bid        매수 제약 조건
     * @param ask        매도 제약 조건
     * @param maxTotal   최대 주문 금액
     * @param state      마켓 상태 (active 등)
     */
    public record Market(
            String id,
            String name,
            List<String> orderTypes,
            List<String> orderSides,
            List<String> bidTypes,
            List<String> askTypes,
            Constraint bid,
            Constraint ask,
            BigDecimal maxTotal,
            String state
    ) {
    }

    /**
     * 주문 제약 조건
     *
     * @param currency 화폐 코드
     * @param minTotal 최소 주문 금액
     */
    public record Constraint(
            String currency,
            BigDecimal minTotal
    ) {
    }

    /**
     * 계정 정보
     *
     * @param currency            화폐 코드
     * @param balance             잔고
     * @param locked              묶여있는 금액
     * @param avgBuyPrice         평균 매수 단가
     * @param avgBuyPriceModified 평균 매수 단가 수정 여부
     * @param unitCurrency        단위 화폐
     */
    public record Account(
            String currency,
            BigDecimal balance,
            BigDecimal locked,
            BigDecimal avgBuyPrice,
            Boolean avgBuyPriceModified,
            String unitCurrency
    ) {
    }
}
