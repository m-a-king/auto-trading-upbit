package com.making.auto_trading_with_upbit_api.indicator;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.indicator.domain.RsiResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class RsiIndicatorServiceTest {

    @Autowired
    private RsiIndicatorService rsiIndicatorService;

    @BeforeEach
    void setUp() throws InterruptedException {
        // Rate Limit 방지
        Thread.sleep(150);
    }

    @Test
    @DisplayName("RSI 계산 - 기본 기간 14일")
    void testCalculateRsi_defaultPeriod() {
        // given
        final String market = "KRW-BTC";

        // when
        final RsiResult result = rsiIndicatorService.calculateDefault(market);

        // then
        assertThat(result).isNotNull();
        assertThat(result.value()).isNotNull();
        assertThat(result.value()).isBetween(BigDecimal.ZERO, new BigDecimal("100"));
        assertThat(result.period()).isEqualTo(14);
        assertThat(result.au()).isNotNull();
        assertThat(result.ad()).isNotNull();
        assertThat(result.rs()).isNotNull();

        // RSI 상태 확인
        assertThat(result.getStatus()).isIn("과매수", "과매도", "중립");

        System.out.println("RSI: " + result.value());
        System.out.println("AU: " + result.au());
        System.out.println("AD: " + result.ad());
        System.out.println("RS: " + result.rs());
        System.out.println("상태: " + result.getStatus());
    }

    @Test
    @DisplayName("RSI 계산 - 사용자 지정 기간")
    void testCalculateRsi_customPeriod() {
        // given
        final String market = "KRW-BTC";
        final int period = 20;

        // when
        final RsiResult result = rsiIndicatorService.calculate(market, period);

        // then
        assertThat(result).isNotNull();
        assertThat(result.value()).isBetween(BigDecimal.ZERO, new BigDecimal("100"));
        assertThat(result.period()).isEqualTo(period);
    }

    @Test
    @DisplayName("RSI 과매수 상태 판단")
    void testRsiOverbought() {
        // given
        final String market = "KRW-BTC";

        // when
        final RsiResult result = rsiIndicatorService.calculateDefault(market);

        // then
        if (result.value().compareTo(new BigDecimal("70")) >= 0) {
            assertThat(result.isOverbought()).isTrue();
            assertThat(result.isOversold()).isFalse();
            assertThat(result.getStatus()).isEqualTo("과매수");
        }
    }

    @Test
    @DisplayName("RSI 과매도 상태 판단")
    void testRsiOversold() {
        // given
        final String market = "KRW-BTC";

        // when
        final RsiResult result = rsiIndicatorService.calculateDefault(market);

        // then
        if (result.value().compareTo(new BigDecimal("30")) <= 0) {
            assertThat(result.isOversold()).isTrue();
            assertThat(result.isOverbought()).isFalse();
            assertThat(result.getStatus()).isEqualTo("과매도");
        }
    }

    @Test
    @DisplayName("RSI 중립 상태 판단")
    void testRsiNeutral() {
        // given
        final String market = "KRW-BTC";

        // when
        final RsiResult result = rsiIndicatorService.calculateDefault(market);

        // then
        final BigDecimal value = result.value();
        if (value.compareTo(new BigDecimal("30")) > 0 && value.compareTo(new BigDecimal("70")) < 0) {
            assertThat(result.isOverbought()).isFalse();
            assertThat(result.isOversold()).isFalse();
            assertThat(result.getStatus()).isEqualTo("중립");
        }
    }

    @Test
    @DisplayName("여러 마켓의 RSI 계산")
    void testCalculateRsi_multipleMarkets() {
        // given
        final String[] markets = {"KRW-BTC", "KRW-ETH", "KRW-XRP"};

        // when & then
        for (final String market : markets) {
            final RsiResult result = rsiIndicatorService.calculateDefault(market);

            assertThat(result).isNotNull();
            assertThat(result.value()).isBetween(BigDecimal.ZERO, new BigDecimal("100"));

            System.out.println(market + " RSI: " + result.value() + " (" + result.getStatus() + ")");

            // Rate Limit 방지
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
