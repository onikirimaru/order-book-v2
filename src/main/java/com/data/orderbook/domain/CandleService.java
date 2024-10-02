package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.model.FirstTick;
import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.model.PriceLevel;
import com.data.orderbook.domain.model.PriceLevelUpdate;
import com.data.orderbook.domain.model.Tick;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vavr.control.Try;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
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
    private final CandleInstantFixer candleInstantFixer;
    private final CandleInstantFixer instantFixer;

    public CandleService(
            @Value("${order-book.candle.rounding-mode}") RoundingMode roundingMode,
            OrderBookServicePort orderBookService,
            CandlePublisherPort candlePublisher,
            ClockProvider clockProvider,
            CandleInstantFixer candleInstantFixer) {
        this.orderBookService = orderBookService;
        this.candlePublisher = candlePublisher;
        this.clockProvider = clockProvider;
        this.instantFixer = candleInstantFixer;
        this.roundingMode = roundingMode;
        this.candleInstantFixer = candleInstantFixer;
    }

    @Scheduled(cron = "0 * * * * *")
    public void dump() throws JsonProcessingException {
        var candleInstant = instantFixer.fix(Instant.now(clockProvider.clock()));
        // Fixing candle instant
        // We need a candle per pair
        log.info("'{}' Candle dump started", candleInstant.getEpochSecond());
        orderBookService.ticks(candleInstant).entrySet().stream()
                .flatMap(tickEntry -> tickEntry
                        .getValue()
                        .map(t -> calculateCandle(candleInstant, tickEntry.getKey(), t))
                        .orElseGet(() -> handleEmptyCandle(tickEntry.getKey(), candleInstant)))
                .forEach(c -> Try.run(() -> candlePublisher.publish(c))
                        .orElseRun(e -> log.error("Error publishing candle '{}'", candleInstant, e)));
    }

    private Stream<OrderBookCandle> handleEmptyCandle(String key, Instant instant) {
        log.warn("Empty candle '{}' '{}', key", key, instant);
        return Stream.empty();
    }

    private Stream<OrderBookCandle> calculateCandle(Instant tickInstant, String pair, Tick tick) {
        if (tick instanceof FirstTick) {
            log.info("Skipping first tick '{}'", tick);
            return Stream.empty();
        }
        var open = midPrice(tick.startingA().price(), tick.startingB().price());
        var close = midPrice(tick.lastA().price(), tick.lastB().price());
        var highestA = tick.as().stream()
                .map(PriceLevelUpdate::price)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        var lowestA = tick.as().stream()
                .map(PriceLevelUpdate::price)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        var highestB = tick.bs().stream()
                .map(PriceLevelUpdate::price)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        var lowestB = tick.bs().stream()
                .map(PriceLevelUpdate::price)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        var high = midPrice(highestA, lowestB);
        var low = midPrice(lowestA, highestB);
        // Sanity check
        if (highestB.compareTo(lowestA) >= 0) {
            log.warn(
                    "Bad tick '{}' '{}', highest Bid '{}' bigger than lowest Ask '{}'",
                    pair,
                    tickInstant,
                    highestA,
                    lowestA);
        }
        return Stream.of(new OrderBookCandle(
                UUID.randomUUID().toString(),
                pair,
                tickInstant.getEpochSecond(),
                open,
                high,
                low,
                close,
                tick.totalUpdates()));
    }

    private BigDecimal midPrice(BigDecimal a, BigDecimal b) {
        return a.add(b).divide(BigDecimal.TWO, roundingMode);
    }
}
