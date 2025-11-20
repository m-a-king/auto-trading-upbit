package com.making.auto_trading_with_upbit_api.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.client.config.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.dto.UpbitApiResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class UpbitClientTest {

    @Autowired
    private UpbitClient upbitClient;

    @Test
    @DisplayName("마켓 목록 조회 - 파라미터 미포함")
    void testGetAllMarkets() {
        final UpbitApiResponse response = upbitClient.requestGet(UpbitApiPath.MARKET_ALL.getPath());

        assertSuccessResponse(response);
        assertThat(response.data()).isNotEmpty();
    }

    @Test
    @DisplayName("분 캔들 조회 - 파라미터 포함")
    void testGetMinuteCandles() {
        final Map<String, List<String>> params = Map.of(
                "market", List.of("KRW-BTC"),
                "count", List.of("10")
        );

        final UpbitApiResponse response = upbitClient.requestGet(
                UpbitApiPath.CANDLES_MINUTES.withUnit(1),
                params
        );

        assertSuccessResponse(response);
        assertThat(response.data()).isNotEmpty();
        assertThat(response.data().size()).isEqualTo(10);
    }

    private void assertSuccessResponse(final UpbitApiResponse response) {
        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().isArray()).isTrue();
    }
}
