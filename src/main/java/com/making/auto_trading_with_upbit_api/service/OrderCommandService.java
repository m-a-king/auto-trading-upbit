package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.service.dto.Order;
import java.math.BigDecimal;
import java.util.List;

/**
 * 주문 명령 서비스 인터페이스
 * <p>
 * 구현체: UpbitOrderCommandService (실제 주문), TestOrderCommandService (시뮬레이션)
 */
public interface OrderCommandService {

    // ==================== 주문 생성 ====================

    /**
     * 지정가 매수 주문 생성
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param volume 주문 수량
     * @param price  주문 가격
     * @return 생성된 주문 정보
     */
    Order createLimitBuyOrder(String market, BigDecimal volume, BigDecimal price);

    /**
     * 지정가 매도 주문 생성
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param volume 주문 수량
     * @param price  주문 가격
     * @return 생성된 주문 정보
     */
    Order createLimitSellOrder(String market, BigDecimal volume, BigDecimal price);

    /**
     * 시장가 매수 주문 생성
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param price  주문 금액 (KRW)
     * @return 생성된 주문 정보
     */
    Order createMarketBuyOrder(String market, BigDecimal price);

    /**
     * 시장가 매도 주문 생성
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param volume 주문 수량
     * @return 생성된 주문 정보
     */
    Order createMarketSellOrder(String market, BigDecimal volume);

    // ==================== 주문 취소 ====================

    /**
     * 개별 주문 취소
     *
     * @param uuid 주문 고유 식별자
     * @return 취소된 주문 정보
     */
    Order cancelOrder(String uuid);

    /**
     * id로 주문 목록 취소
     *
     * @param ids 주문 고유 식별자 목록
     * @return 취소된 주문 목록
     */
    List<Order> cancelOrders(List<String> ids);

    /**
     * 주문 일괄 취소
     *
     * @param market 마켓 코드 (null이면 전체 취소)
     * @return 취소된 주문 수
     */
    int cancelAllOrders(String market);

    // ==================== 취소 후 재주문 ====================

    /**
     * 취소 후 재주문 (지정가)
     *
     * @param uuid   취소할 주문 uuid
     * @param volume 새 주문 수량
     * @param price  새 주문 가격
     * @return 새로 생성된 주문 정보
     */
    Order cancelAndNewOrder(String uuid, BigDecimal volume, BigDecimal price);
}