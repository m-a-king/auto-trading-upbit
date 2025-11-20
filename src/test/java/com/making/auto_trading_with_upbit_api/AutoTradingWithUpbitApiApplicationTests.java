package com.making.auto_trading_with_upbit_api;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class AutoTradingWithUpbitApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
