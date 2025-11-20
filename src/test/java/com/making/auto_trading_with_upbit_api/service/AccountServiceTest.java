package com.making.auto_trading_with_upbit_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.making.auto_trading_with_upbit_api.config.TestContainersConfiguration;
import com.making.auto_trading_with_upbit_api.constants.CurrencyCode;
import com.making.auto_trading_with_upbit_api.service.dto.Account;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("전체 잔고 조회")
    void testGetAccounts() {
        // given (API 키가 설정되어 있음)

        // when
        final List<Account> accounts = accountService.getAccounts();

        // then
        assertThat(accounts).isNotNull();
        assertThat(accounts).isNotEmpty();
    }

    @Test
    @DisplayName("KRW 보유량 조회")
    void testGetKrwBalance() {
        // given (API 키가 설정되어 있음)

        // when
        final BigDecimal krwBalance = accountService.getKrwBalance();

        // then
        assertThat(krwBalance).isNotNull();
        assertThat(krwBalance).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("특정 코인 보유량 조회 - KRW")
    void testGetCoinBalanceKrw() {
        // given
        final CurrencyCode currencyCode = CurrencyCode.KRW;

        // when
        final BigDecimal balance = accountService.getCoinBalance(currencyCode);

        // then
        assertThat(balance).isNotNull();
        assertThat(balance).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("특정 코인 보유량 조회 - 보유하지 않은 코인")
    void testGetCoinBalanceNotOwned() {
        // given
        final CurrencyCode currencyCode = CurrencyCode.BTC;

        // when
        final BigDecimal balance = accountService.getCoinBalance(currencyCode);

        // then
        assertThat(balance).isNotNull();
        assertThat(balance).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("매수 가능 금액 계산")
    void testGetAvailableBuyAmount() {
        // given (API 키가 설정되어 있음)

        // when
        final BigDecimal availableAmount = accountService.getAvailableBuyAmount();

        // then
        assertThat(availableAmount).isNotNull();
        assertThat(availableAmount).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }
}