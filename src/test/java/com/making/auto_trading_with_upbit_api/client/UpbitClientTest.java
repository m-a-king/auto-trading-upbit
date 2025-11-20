package com.making.auto_trading_with_upbit_api.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class UpbitClientTest {

    @Autowired
    private UpbitClient upbitClient;

    @Test
    @DisplayName("마켓 목록 조회 - 파라미터 미포함")
    void testGetAllMarkets() {
        // given
        final UpbitApiPath apiPath = UpbitApiPath.MARKET_ALL;

        // when
        final UpbitApiResponse response = upbitClient.requestGet(apiPath);

        // then
        assertSuccessResponse(response);
        assertThat(response.data()).isNotEmpty();
    }

    @Test
    @DisplayName("분 캔들 조회 - 파라미터 포함")
    void testGetMinuteCandles() {
        // given
        final UpbitApiPath apiPath = UpbitApiPath.CANDLES_MINUTES;
        final Integer unit = 1;
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("market", "KRW-BTC");
        params.add("count", "10");

        // when
        final UpbitApiResponse response = upbitClient.requestGet(apiPath, unit, params);

        // then
        assertSuccessResponse(response);
        assertThat(response.data()).isNotEmpty();
        assertThat(response.data().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("계정 잔고 조회 - 인증 필요")
    void testGetAccounts() {
        // given
        final UpbitApiPath apiPath = UpbitApiPath.ACCOUNTS;

        // when
        final UpbitApiResponse response = upbitClient.requestGet(apiPath);

        // then
        assertSuccessResponse(response);
    }

    @Test
    @DisplayName("체결 대기 주문 목록 조회 - 인증 필요, 파라미터 포함")
    void testGetOpenOrders() {
        // given
        final UpbitApiPath apiPath = UpbitApiPath.ORDERS_OPEN;
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("market", "KRW-BTC");
        params.add("state", "wait");

        // when
        final UpbitApiResponse response = upbitClient.requestGet(apiPath, params);

        // then
        System.out.println(response);
        assertSuccessResponse(response);
    }


    private void assertSuccessResponse(final UpbitApiResponse response) {
        assertThat(response.success()).isTrue();
        assertThat(response.data().isArray()).isTrue();
    }
}
