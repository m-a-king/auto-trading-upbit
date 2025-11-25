package com.making.auto_trading_with_upbit_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.service.dto.Order;
import com.making.auto_trading_with_upbit_api.service.dto.OrderChance;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class OrderServiceTest {

    @Autowired
    private OrderQueryService orderQueryService;

    @Test
    @DisplayName("주문 가능 정보 조회")
    void testGetOrderChance() {
        // given
        final String market = "KRW-BTC";

        // when
        final OrderChance orderChance = orderQueryService.getOrderChance(market);

        // then
        assertThat(orderChance).isNotNull();
        assertThat(orderChance.market()).isNotNull();
        assertThat(orderChance.market().id()).isEqualTo(market);
    }

    @Test
    @DisplayName("체결 대기 주문 목록 조회")
    void testGetOpenOrders() {
        // given
        // (기존 체결 대기 주문이 있을 수도 있고 없을 수도 있음)

        // when
        final List<Order> openOrders = orderQueryService.getOpenOrders();

        // then
        assertThat(openOrders).isNotNull();
        // 체결 대기 주문이 있다면, state가 'wait' 또는 'watch'여야 함
        openOrders.forEach(order -> {
            assertThat(order.state()).isIn("wait", "watch");
        });
    }
}
