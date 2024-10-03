package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import io.vavr.control.Try;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CandleService {

    private final OrderBookServicePort orderBookService;

    private final CandlePublisherPort candlePublisher;
    private final ClockProvider clockProvider;
    private final RoundingMode roundingMode;

    public CandleService(
            @Value("${order-book.candle.rounding-mode}") RoundingMode roundingMode,
            OrderBookServicePort orderBookService,
            CandlePublisherPort candlePublisher,
            ClockProvider clockProvider) {
        this.orderBookService = orderBookService;
        this.candlePublisher = candlePublisher;
        this.clockProvider = clockProvider;
        this.roundingMode = roundingMode;
    }

    @Scheduled(
            fixedRateString = "${order-book.candle.rate-millis}",
            initialDelayString = "${order-book.candle.delay-millis}")
    public void dump() {
        var candleInstant = Instant.now(clockProvider.clock());
        // Skip first candle
        // We need a candle per pair
        log.info("'{}' Candle dump started", candleInstant.getEpochSecond());
        orderBookService.getCandle(candleInstant).values().forEach(c -> Try.run(() -> candlePublisher.publish(c))
                .orElseRun(e -> log.error("Error publishing candle '{}'", candleInstant, e)));
    }
}
