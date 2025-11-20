package com.making.auto_trading_with_upbit_api.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.client.config.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.dto.UpbitApiResponse;
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
    @DisplayName("마켓 목록 조회")
    void testGetAllMarkets() {
        final UpbitApiResponse response = upbitClient.requestGet(UpbitApiPath.MARKET_ALL.getPath());

        assertSuccessResponse(response);
        assertThat(response.data()).isNotEmpty();
    }

    private void assertSuccessResponse(final UpbitApiResponse response) {
        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().isArray()).isTrue();
    }
}
