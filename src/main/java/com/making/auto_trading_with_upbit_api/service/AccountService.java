package com.making.auto_trading_with_upbit_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.service.dto.Account;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UpbitClient upbitClient;
    private final ObjectMapper objectMapperForUpbit;

    /**
     * 전체 잔고 조회
     */
    public List<Account> getAccounts() {
        final UpbitApiResponse response = upbitClient.requestGet(UpbitApiPath.ACCOUNTS);

        if (response.success()) {
            return parseAccounts(response.data());
        }

        log.error("Failed to get accounts: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get accounts: " + response.errorMessage());
    }

    /**
     * KRW 보유량 조회
     */
    public BigDecimal getKrwBalance() {
        return getCoinBalance("KRW");
    }

    /**
     * 특정 코인 보유량 조회
     *
     * @param currency 화폐 코드 (KRW, BTC, ETH 등)
     * @return 보유량 (없으면 0)
     */
    public BigDecimal getCoinBalance(final String currency) {
        final List<Account> accounts = getAccounts();

        return accounts.stream()
                .filter(account -> currency.equals(account.currency()))
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

    private List<Account> parseAccounts(final JsonNode data) {
        final List<Account> accounts = new ArrayList<>();

        if (data == null || !data.isArray()) {
            return accounts;
        }

        for (final JsonNode node : data) {
            try {
                final Account account = objectMapperForUpbit.treeToValue(node, Account.class);
                accounts.add(account);
            } catch (final Exception e) {
                log.error("Failed to parse account: {}", node, e);
            }
        }

        return accounts;
    }
}
