package com.making.auto_trading_with_upbit_api.indicator;

import com.making.auto_trading_with_upbit_api.client.path.CandleUpbitApiPath.CandleUnit;
import com.making.auto_trading_with_upbit_api.indicator.domain.RsiResult;
import com.making.auto_trading_with_upbit_api.service.CandleDataService;
import com.making.auto_trading_with_upbit_api.service.dto.Candle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * RSI (Relative Strength Index) 지표 계산 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RsiIndicatorService {

    private static final int DEFAULT_RSI_PERIOD = 14;
    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final CandleDataService candleDataService;

    /**
     * RSI (Relative Strength Index) 계산
     *
     * @param market 마켓 코드 (예: KRW-BTC)
     * @param period RSI 계산 기간 (기본: 14)
     * @return RSI 계산 결과
     */
    public RsiResult calculate(final String market, final int period) {
        // RSI 계산에 필요한 캔들 개수: period * 2 (충분한 데이터 확보)
        final int requiredCandles = period * 2;

        // 일봉 캔들 데이터 조회 (최신순)
        final List<Candle> candles = candleDataService.getCandles(market, CandleUnit.DAY, requiredCandles);

        if (candles.size() < period + 1) {
            throw new IllegalArgumentException(
                    "RSI 계산에 필요한 캔들 개수 부족: 필요=" + (period + 1) + ", 실제=" + candles.size()
            );
        }

        // 캔들 데이터는 최신순이므로 역순으로 처리 (과거 → 현재)
        return calculateRsiFromCandles(candles.reversed(), period);
    }

    /**
     * RSI 계산 (기본 기간: 14일)
     *
     * @param market 마켓 코드
     * @return RSI 계산 결과
     */
    public RsiResult calculateDefault(final String market) {
        return calculate(market, DEFAULT_RSI_PERIOD);
    }

    /**
     * 캔들 데이터로부터 RSI 계산
     */
    private RsiResult calculateRsiFromCandles(final List<Candle> candles, final int period) {
        // 1단계: 가격 변화량 계산 (change_price 활용)
        BigDecimal sumGains = BigDecimal.ZERO;
        BigDecimal sumLosses = BigDecimal.ZERO;

        // 초기 period개 데이터로 평균 계산
        for (int i = 0; i < period; i++) {
            final BigDecimal changePrice = candles.get(i).changePrice();

            // changePrice가 null인 경우 0으로 간주 (변화 없음)
            if (changePrice == null) {
                continue;
            }

            if (changePrice.compareTo(BigDecimal.ZERO) > 0) {
                sumGains = sumGains.add(changePrice);
            } else {
                sumLosses = sumLosses.add(changePrice.abs());
            }
        }

        // 2단계: 초기 AU, AD 계산
        BigDecimal au = sumGains.divide(BigDecimal.valueOf(period), SCALE, ROUNDING_MODE);
        BigDecimal ad = sumLosses.divide(BigDecimal.valueOf(period), SCALE, ROUNDING_MODE);

        // 3단계: 누적 평균 갱신 (나머지 데이터)
        final BigDecimal periodMinusOne = BigDecimal.valueOf(period - 1);
        final BigDecimal periodDecimal = BigDecimal.valueOf(period);

        for (int i = period; i < candles.size(); i++) {
            final BigDecimal changePrice = candles.get(i).changePrice();

            // changePrice가 null인 경우 0으로 간주 (변화 없음)
            if (changePrice == null) {
                continue;
            }

            final BigDecimal gain = changePrice.compareTo(BigDecimal.ZERO) > 0 ? changePrice : BigDecimal.ZERO;
            final BigDecimal loss = changePrice.compareTo(BigDecimal.ZERO) < 0 ? changePrice.abs() : BigDecimal.ZERO;

            au = au.multiply(periodMinusOne).add(gain).divide(periodDecimal, SCALE, ROUNDING_MODE);
            ad = ad.multiply(periodMinusOne).add(loss).divide(periodDecimal, SCALE, ROUNDING_MODE);
        }

        // 4단계: RS 및 RSI 계산
        final BigDecimal rs;
        final BigDecimal rsi;

        if (ad.compareTo(BigDecimal.ZERO) == 0) {
            // AD가 0이면 RS는 무한대, RSI는 100
            rs = BigDecimal.ZERO;
            rsi = new BigDecimal("100");
        } else {
            rs = au.divide(ad, SCALE, ROUNDING_MODE);
            // RSI = 100 - (100 / (1 + RS))
            final BigDecimal onePlusRs = BigDecimal.ONE.add(rs);
            final BigDecimal hundred = new BigDecimal("100");
            rsi = hundred.subtract(hundred.divide(onePlusRs, SCALE, ROUNDING_MODE));
        }

        log.debug("RSI 계산 완료 - period: {}, RSI: {}, AU: {}, AD: {}, RS: {}", period, rsi, au, ad, rs);

        return RsiResult.of(rsi, au, ad, rs, period);
    }
}
