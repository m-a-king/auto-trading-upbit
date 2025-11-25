package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitRequest;
import com.making.auto_trading_with_upbit_api.client.path.OrderUpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.util.JsonConverter;
import com.making.auto_trading_with_upbit_api.service.dto.Order;
import com.making.auto_trading_with_upbit_api.service.dto.OrderChance;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 업비트 주문 조회 서비스 구현체 (실제 API 호출)
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UpbitOrderQueryService implements OrderQueryService {

    private final UpbitClient upbitClient;
    private final JsonConverter jsonConverter;

    @Override
    public OrderChance getOrderChance(final String market) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("market", market);

        final UpbitRequest request = UpbitRequest.of(OrderUpbitApiPath.GET_ORDER_CHANCE, params);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toObject(response.data(), OrderChance.class);
        }

        log.error("Failed to get order chance: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get order chance: " + response.errorMessage());
    }

    @Override
    public Order getOrder(final String uuidOrIdentifier) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("uuid", uuidOrIdentifier);

        final UpbitRequest request = UpbitRequest.of(OrderUpbitApiPath.GET_ORDER, params);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toObject(response.data(), Order.class);
        }

        log.error("Failed to get order: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get order: " + response.errorMessage());
    }

    @Override
    public List<Order> getOrders(final List<String> ids) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        ids.forEach(id -> params.add("uuids[]", id));

        final UpbitRequest request = UpbitRequest.of(OrderUpbitApiPath.GET_ORDERS, params);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toList(response.data(), Order.class);
        }

        log.error("Failed to get orders: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get orders: " + response.errorMessage());
    }

    @Override
    public List<Order> getOpenOrders() {
        final UpbitRequest request = UpbitRequest.of(OrderUpbitApiPath.GET_OPEN_ORDERS);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toList(response.data(), Order.class);
        }

        log.error("Failed to get open orders: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get open orders: " + response.errorMessage());
    }

    @Override
    public List<Order> getClosedOrders(final int window) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("days", String.valueOf(window));

        final UpbitRequest request = UpbitRequest.of(OrderUpbitApiPath.GET_CLOSED_ORDERS, params);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toList(response.data(), Order.class);
        }

        log.error("Failed to get closed orders: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get closed orders: " + response.errorMessage());
    }
}
