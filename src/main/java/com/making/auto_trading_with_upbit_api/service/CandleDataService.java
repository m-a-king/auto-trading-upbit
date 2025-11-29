package com.making.auto_trading_with_upbit_api.service;

import com.making.auto_trading_with_upbit_api.client.UpbitClient;
import com.making.auto_trading_with_upbit_api.client.constants.ApiConstants;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitApiResponse;
import com.making.auto_trading_with_upbit_api.client.dto.UpbitRequest;
import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath;
import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandleMinuteUnit;
import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandlePathParam;
import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandleUnit;
import com.making.auto_trading_with_upbit_api.client.util.JsonConverter;
import com.making.auto_trading_with_upbit_api.service.dto.Candle;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 캔들 데이터 조회 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CandleDataService {

    private final UpbitClient upbitClient;
    private final JsonConverter jsonConverter;

    /**
     * 캔들 조회 (초/일/주/월/연)
     *
     * @param market     마켓 코드 (예: KRW-BTC)
     * @param candleUnit 캔들 단위 (SECOND, DAY, WEEK, MONTH, YEAR)
     * @param count      조회할 캔들 개수 (최대 200)
     * @return 캔들 목록 (최신순)
     */
    public List<Candle> getCandles(
            final String market,
            final CandleUnit candleUnit,
            final int count
    ) {
        final CandlePathParam pathParam = CandlePathParam.from(candleUnit);
        return getCandles(market, pathParam, count);
    }

    /**
     * 분 캔들 조회
     *
     * @param market     마켓 코드 (예: KRW-BTC)
     * @param minuteUnit 분 단위 (ONE, THREE, FIVE, TEN, FIFTEEN, THIRTY, SIXTY, TWO_HUNDRED_FORTY)
     * @param count      조회할 캔들 개수 (최대 200)
     * @return 캔들 목록 (최신순)
     */
    public List<Candle> getCandles(
            final String market,
            final CandleMinuteUnit minuteUnit,
            final int count
    ) {
        final CandlePathParam pathParam = CandlePathParam.of(CandleUnit.MINUTE, minuteUnit);
        return getCandles(market, pathParam, count);
    }

    private List<Candle> getCandles(
            final String market,
            final CandlePathParam pathParam,
            final int count
    ) {
        final int validCount = Math.min(count, ApiConstants.MAX_CANDLE_COUNT);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ApiConstants.MARKET, market);
        params.add(ApiConstants.COUNT, String.valueOf(validCount));

        final UpbitRequest request = UpbitRequest.of(CandleUpbitApiPath.CANDLES, pathParam, params);
        final UpbitApiResponse response = upbitClient.requestGet(request);

        if (response.success()) {
            return jsonConverter.toList(response.data(), Candle.class);
        }

        log.error("Failed to get candles: {} - {}", response.errorName(), response.errorMessage());
        throw new RuntimeException("Failed to get candles: " + response.errorMessage());
    }
}
