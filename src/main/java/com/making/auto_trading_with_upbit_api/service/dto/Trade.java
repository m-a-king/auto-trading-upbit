package com.making.auto_trading_with_upbit_api.service.dto;

import java.math.BigDecimal;

/**
 * 체결 내역 정보 (개별 주문 조회 시 trades 필드에 포함)
 *
 * @param market    마켓 코드 (예: KRW-BTC)
 * @param uuid      체결 고유 식별자
 * @param price     체결 가격
 * @param volume    체결 수량
 * @param funds     체결 금액 (price * volume)
 * @param trend     가격 변화 추세 (up, down)
 * @param createdAt 체결 시각
 * @param side      체결 방향 (bid: 매수, ask: 매도)
 */
public record Trade(
        String market,
        String uuid,
        BigDecimal price,
        BigDecimal volume,
        BigDecimal funds,
        String trend,
        String createdAt,
        String side
) {
}
