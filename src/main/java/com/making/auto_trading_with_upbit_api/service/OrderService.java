package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.util.JsonConverter;
import com.making.auto_trading_with_upbit_api.service.dto.Order;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final UpbitClient upbitClient;
    private final JsonConverter jsonConverter;

    /**
     * 시장가 매수 주문 생성
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param price  주문 금액 (KRW)
     * @return 생성된 주문 정보
     */
    public Order createMarketBuyOrder(final String market, final BigDecimal price) {
        return createOrder(UpbitApiPath.ORDERS_POST, market, "bid", "price", price, null);
    }

    /**
     * 시장가 매수 주문 생성 (테스트)
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param price  주문 금액 (KRW)
     * @return 생성된 주문 정보
     */
    public Order createMarketBuyOrderTest(final String market, final BigDecimal price) {
        return createOrder(UpbitApiPath.ORDERS_TEST, market, "bid", "price", price, null);
    }

    private Order createOrder(
            final UpbitApiPath apiPath,
            final String market,
            final String side,
            final String ordType,
            final BigDecimal price,
            final BigDecimal volume
    ) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("market", market);
        params.add("side", side);
        params.add("ord_type", ordType);

        if (price != null) {
            params.add("price", price.toPlainString());
        }

        if (volume != null) {
            params.add("volume", volume.toPlainString());
        }

        final UpbitApiResponse response = upbitClient.requestPost(apiPath, params);

        if (response.success()) {
            return jsonConverter.toObject(response.data(), Order.class);
        }

        log.error("Failed to create order: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to create order: " + response.errorMessage());
    }
}
