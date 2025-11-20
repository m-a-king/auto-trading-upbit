package com.making.auto_trading_with_upbit_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.constants.UpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.service.dto.Ticker;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarketDataService {

    private final UpbitClient upbitClient;
    private final ObjectMapper objectMapperForUpbit;

    /**
     * 현재가 조회 - 단일 마켓
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @return 현재가 정보
     */
    public Ticker getTicker(final String market) {
        final List<Ticker> tickers = getTickers(List.of(market));

        if (tickers.isEmpty()) {
            throw new RuntimeException("Failed to get ticker for market: " + market);
        }

        return tickers.getFirst();
    }

    /**
     * 현재가 조회 - 여러 마켓
     *
     * @param markets 마켓 코드 리스트
     * @return 현재가 정보 리스트
     */
    public List<Ticker> getTickers(final List<String> markets) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        markets.forEach(market -> params.add("markets", market));

        final UpbitApiResponse response = upbitClient.requestGet(UpbitApiPath.TICKER, params);

        if (!response.success()) {
            log.error("Failed to get tickers: {} - {}", response.errorName(), response.errorMessage());
            throw new RuntimeException("Failed to get tickers: " + response.errorMessage());
        }

        return parseTickers(response.data());
    }

    /**
     * 현재 거래가 조회
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @return 현재 거래가
     */
    public BigDecimal getCurrentPrice(final String market) {
        final Ticker ticker = getTicker(market);
        return ticker.tradePrice();
    }

    private List<Ticker> parseTickers(final JsonNode data) {
        final List<Ticker> tickers = new ArrayList<>();

        if (data == null || !data.isArray()) {
            return tickers;
        }

        for (final JsonNode node : data) {
            try {
                final Ticker ticker = objectMapperForUpbit.treeToValue(node, Ticker.class);
                tickers.add(ticker);
            } catch (final Exception e) {
                log.error("Failed to parse ticker: {}", node, e);
            }
        }

        return tickers;
    }
}
