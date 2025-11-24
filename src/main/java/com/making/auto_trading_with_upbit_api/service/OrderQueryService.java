package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.service.dto.Order;
import com.making.auto_trading_with_upbit_api.service.dto.OrderChance;
import java.util.List;

/**
 * 주문 조회 서비스 인터페이스
 * <p>
 * 구현체: UpbitOrderQueryService (실제 API), TestOrderQueryService (시뮬레이션)
 */
public interface OrderQueryService {

    /**
     * 페어별 주문 가능 정보 조회
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @return 주문 가능 정보 (수수료, 최소 주문 금액 등)
     */
    OrderChance getOrderChance(String market);

    /**
     * 개별 주문 조회
     *
     * @param id 주문 고유 식별자 (uuid) 또는 사용자 지정 주문 ID (identifier)
     * @return 주문 정보
     */
    Order getOrder(String id);

    /**
     * id로 주문 목록 조회
     *
     * @param ids 주문 고유 식별자 목록
     * @return 주문 목록
     */
    List<Order> getOrders(List<String> ids);

    /**
     * 체결 대기 주문 목록 조회
     *
     * @return 체결 대기 중인 주문 목록
     */
    List<Order> getOpenOrders();

    /**
     * 종료된 주문 목록 조회
     *
     * @param window 조회 기간 (최대 7일)
     * @return 종료된 주문 목록 (체결 또는 취소)
     */
    List<Order> getClosedOrders(int window);
}