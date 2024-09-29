package com.data.orderbook.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter(onMethod = @__(@JsonProperty))
@Accessors(fluent = true)
@ToString
public class OrderBook {

    public static final long SECONDS = TimeUnit.MINUTES.toSeconds(1);
    private final Instant instant;
    private final String pair;
    private final ConcurrentHashMap<Instant, Tick> ticks = new ConcurrentHashMap<>();
    private final ClockProvider clockProvider;
    private Instant lastUpdate = Instant.MIN;
    private Tick currentTick;

    public OrderBook(String pair, ClockProvider clockProvider) {
        this.clockProvider = clockProvider;
        this.pair = pair;
        this.instant = Instant.now(clockProvider.clock());
        var firstBucket = calculateBucket(instant);
        this.currentTick = new FirstTick(firstBucket);
        this.ticks.put(firstBucket, currentTick);
    }

    public OrderBook ingest(OrderBookUpdate update) {
        // FIXME This needs to be fixed to microseconds and we must check every update
        var nonNullAs = getPriceLevels(update.a());
        var nonNullBs = getPriceLevels(update.b());
        updateTimeStamp(nonNullAs, nonNullBs);
        updateBuckets(nonNullAs, nonNullBs);
        return this;
    }

    private void updateTimeStamp(List<PriceLevel> nonNullAs, List<PriceLevel> nonNullBs) {
        var newLastUpdate = Stream.concat(nonNullAs.stream(), nonNullBs.stream())
                .map(PriceLevel::timestamp)
                .max(Comparator.comparing(Instant::toEpochMilli));
        // Book last update should always bigger than previous
        newLastUpdate.ifPresent(nlu -> {
            if (nlu.isBefore(lastUpdate)) {
                log.warn("Update contains data in the past: '{}' vs '{}'", nlu, lastUpdate);
            }
        });
        lastUpdate = newLastUpdate.orElse(lastUpdate);
    }

    private void updateBuckets(List<PriceLevel> nonNullAs, List<PriceLevel> nonNullBs) {
        nonNullAs.forEach(ask -> {
            // Calculate bucket
            final var bucket = calculateBucket(ask.timestamp());
            addA(bucket, ask);
        });
        nonNullBs.forEach(ask -> {
            // Calculate bucket
            final var bucket = calculateBucket(ask.timestamp());
            addB(bucket, ask);
        });
    }

    private static List<PriceLevel> getPriceLevels(List<PriceLevel> maybePriceLevel) {
        return Optional.ofNullable(maybePriceLevel).orElse(List.of());
    }

    private Instant calculateBucket(Instant timestamp) {
        // Bucket is the one that ends in the next minute (need to round epoch seconds to minutes to calculate bucket
        // instant)
        return Instant.ofEpochSecond(((timestamp.getEpochSecond() / SECONDS) + 1) * SECONDS);
    }

    private void addA(Instant bucket, PriceLevel priceLevel) {
        ticks.computeIfAbsent(bucket, b -> new Tick(b, currentTick));
        ticks.computeIfPresent(bucket, (b, tick) -> tick.addAsk(priceLevel));
    }

    private void addB(Instant bucket, PriceLevel priceLevel) {
        ticks.computeIfAbsent(bucket, b -> new Tick(b, currentTick));
        ticks.computeIfPresent(bucket, (b, tick) -> tick.addBid(priceLevel));
    }
}
