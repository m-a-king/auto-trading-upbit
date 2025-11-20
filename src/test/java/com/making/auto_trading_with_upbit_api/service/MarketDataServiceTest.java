package com.making.auto_trading_with_upbit_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.service.dto.Ticker;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class MarketDataServiceTest {

    @Autowired
    private MarketDataService marketDataService;

    @Test
    @DisplayName("현재가 조회 - 단일 마켓")
    void testGetTicker() {
        // given
        final String market = "KRW-BTC";

        // when
        final Ticker ticker = marketDataService.getTicker(market);

        // then
        assertThat(ticker).isNotNull();
        assertThat(ticker.market()).isEqualTo(market);
        assertThat(ticker.tradePrice()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("현재가 조회 - 여러 마켓")
    void testGetTickers() {
        // given
        final List<String> markets = List.of("KRW-BTC", "KRW-ETH");

        // when
        final List<Ticker> tickers = marketDataService.getTickers(markets);

        // then
        assertThat(tickers).isNotNull();
        assertThat(tickers).hasSize(2);
        assertThat(tickers).allMatch(ticker -> ticker.tradePrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("현재 거래가 조회")
    void testGetCurrentPrice() {
        // given
        final String market = "KRW-BTC";

        // when
        final BigDecimal currentPrice = marketDataService.getCurrentPrice(market);

        // then
        assertThat(currentPrice).isNotNull();
        assertThat(currentPrice).isGreaterThan(BigDecimal.ZERO);
    }
}