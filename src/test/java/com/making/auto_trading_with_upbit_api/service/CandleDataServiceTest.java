package com.making.auto_trading_with_upbit_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandleMinuteUnit;
import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandleUnit;
import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.service.dto.Candle;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class CandleDataServiceTest {

    @Autowired
    private CandleDataService candleDataService;

    @BeforeEach
    void setUp() throws InterruptedException {
        // 업비트 API Rate Limit 방지: candle 그룹 초당 10회 제한
        // 각 테스트 전 150ms 대기 (1초에 약 6-7개 테스트 실행)
        Thread.sleep(150);
    }

    @Test
    @DisplayName("초 캔들 조회")
    void testGetSecondCandles() {
        // given
        final String market = "KRW-BTC";
        final int count = 10;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.SECOND, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
        assertThat(candles).allMatch(candle -> candle.tradePrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest
    @EnumSource(value = CandleMinuteUnit.class, names = {"NULL"}, mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("분 캔들 조회 - 모든 단위")
    void testGetMinuteCandles(final CandleMinuteUnit minuteUnit) {
        // given
        final String market = "KRW-BTC";
        final int count = 10;

        // when
        final List<Candle> candles = candleDataService.getMinuteCandles(market, minuteUnit, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
        assertThat(candles).allMatch(candle -> candle.tradePrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("일 캔들 조회")
    void testGetDayCandles() {
        // given
        final String market = "KRW-BTC";
        final int count = 14;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.DAY, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
        assertThat(candles).allMatch(candle -> candle.tradePrice().compareTo(BigDecimal.ZERO) > 0);
        // 일 캔들은 전일 종가, 변동가, 변동율 정보 포함
        assertThat(candles).allMatch(candle -> candle.prevClosingPrice() != null);
    }

    @Test
    @DisplayName("주 캔들 조회")
    void testGetWeekCandles() {
        // given
        final String market = "KRW-BTC";
        final int count = 4;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.WEEK, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
    }

    @Test
    @DisplayName("월 캔들 조회")
    void testGetMonthCandles() {
        // given
        final String market = "KRW-BTC";
        final int count = 12;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.MONTH, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
        assertThat(candles).allMatch(candle -> candle.tradePrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("연 캔들 조회")
    void testGetYearCandles() {
        // given
        final String market = "KRW-BTC";
        final int count = 5;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.YEAR, count);

        // then
        assertThat(candles).isNotNull();
        assertThat(candles).hasSize(count);
        assertThat(candles).allMatch(candle -> candle.market().equals(market));
        assertThat(candles).allMatch(candle -> candle.tradePrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("캔들 양봉/음봉 판단")
    void testCandleBullishBearish() {
        // given
        final String market = "KRW-BTC";
        final int count = 10;

        // when
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.DAY, count);

        // then
        assertThat(candles).isNotNull();
        // 양봉 또는 음봉 중 하나여야 함 (동일가 제외)
        candles.forEach(candle -> {
            if (candle.isBullish()) {
                assertThat(candle.tradePrice()).isGreaterThan(candle.openingPrice());
            } else if (candle.isBearish()) {
                assertThat(candle.tradePrice()).isLessThan(candle.openingPrice());
            }
        });
    }
}
