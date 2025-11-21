package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.path.CommonUpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.util.JsonConverter;
import com.making.auto_trading_with_upbit_api.constants.CurrencyCode;
import com.making.auto_trading_with_upbit_api.service.dto.Account;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UpbitClient upbitClient;
    private final JsonConverter jsonConverter;

    /**
     * 전체 잔고 조회
     */
    public List<Account> getAccounts() {
        final UpbitApiResponse response = upbitClient.requestGet(CommonUpbitApiPath.ACCOUNTS);

        if (response.success()) {
            return jsonConverter.toList(response.data(), Account.class);
        }

        log.error("Failed to get accounts: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get accounts: " + response.errorMessage());
    }

    /**
     * KRW 보유량 조회
     */
    public BigDecimal getKrwBalance() {
        return getCoinBalance(CurrencyCode.KRW);
    }

    /**
     * 특정 코인 보유량 조회
     *
     * @param currencyCode 화폐 코드
     * @return 보유량 (없으면 0)
     */
    public BigDecimal getCoinBalance(final CurrencyCode currencyCode) {
        final List<Account> accounts = getAccounts();

        return accounts.stream()
                .filter(account -> currencyCode.matches(account.currency()))
                .findFirst()
                .map(Account::balance)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 매수 가능 금액 계산 (현재는 KRW 잔고 전체 반환, 향후 수수료/리스크 관리 추가 가능)
     */
    public BigDecimal getAvailableBuyAmount() {
        return getKrwBalance();
    }
}
