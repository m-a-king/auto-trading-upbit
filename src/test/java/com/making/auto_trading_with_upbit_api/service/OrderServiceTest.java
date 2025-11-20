package com.making.auto_trading_with_upbit_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.service.dto.Order;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("시장가 매수 주문 생성 (테스트)")
    void testCreateMarketBuyOrder() {
        // given
        final String market = "KRW-BTC";
        final BigDecimal price = new BigDecimal("6000"); // 최소 주문 금액 이상

        // when
        final Order order = orderService.createMarketBuyOrderTest(market, price);

        // then
        assertThat(order).isNotNull();
        assertThat(order.market()).isEqualTo(market);
        assertThat(order.side()).isEqualTo("bid");
        assertThat(order.ordType()).isEqualTo("price");
        assertThat(order.state()).isIn("wait", "watch", "done");
    }
}
