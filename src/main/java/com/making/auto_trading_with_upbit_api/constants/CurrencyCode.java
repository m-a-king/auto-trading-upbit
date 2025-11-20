package com.making.auto_trading_with_upbit_api.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 화폐 코드
 */
@Getter
@RequiredArgsConstructor
public enum CurrencyCode {

    KRW("KRW"),
    BTC("BTC"),
    ETH("ETH"),
    USDT("USDT");

    private final String code;

    /**
     * 주어진 코드 문자열이 이 화폐 코드와 일치하는지 확인
     *
     * @param code 비교할 코드 문자열
     * @return 일치 여부
     */
    public boolean matches(final String code) {
        return this.code.equals(code);
    }
}