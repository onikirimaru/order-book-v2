package com.data.orderbook.domain.model;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter(onMethod = @__(@JsonProperty))
@Accessors(fluent = true)
@ToString
public class OrderBook {

    private final Instant instant = Instant.now();
    private final String pair;
    private final ConcurrentHashMap<Instant, Tick> ticks = new ConcurrentHashMap<>();
    private Instant lastUpdate = Instant.MIN;

    public OrderBook(String pair) {
        this.pair = pair;
    }

    public OrderBook ingest(OrderBookUpdate update) {

        //FIXME This needs to be fixed to microseconds and we must check every update
        var fixedA = getPriceLevels(update.a());
        var fixedB = getPriceLevels(update.b());
        var newLastUpdate = Stream.concat(fixedA.stream(), fixedB.stream())
                .map(PriceLevel::timestamp)
                .max(Comparator.comparing(Instant::toEpochMilli));
        //Book last update should always bigger than previous
        newLastUpdate.ifPresent(nlu -> {
            if (nlu.isBefore(lastUpdate)) {
                log.warn("Update contains data in the past: '{}' vs '{}'", nlu, lastUpdate);
            }
        });
        lastUpdate = newLastUpdate.orElse(lastUpdate);

        fixedA.forEach(ask -> {
            //Calculate bucket
            final var bucket = calculateBucket(ask.timestamp());
            addA(bucket, ask);
        });
        fixedB.forEach(ask -> {
            //Calculate bucket
            final var bucket = calculateBucket(ask.timestamp());
            addB(bucket, ask);
        });
        return this;
    }

    private static List<PriceLevel> getPriceLevels(List<PriceLevel> maybePriceLevel) {
        return Optional.ofNullable(maybePriceLevel).orElse(List.of());
    }

    private Instant calculateBucket(Instant timestamp) {
        //Bucket is the one that ends in the next minute
        return Instant.ofEpochSecond(((timestamp.getEpochSecond() / 60) + 1) * 60);
    }

    private void addA(Instant bucket, PriceLevel priceLevel) {
        ticks.computeIfAbsent(bucket, Tick::new);
        ticks.computeIfPresent(bucket, (b, tick) -> tick.addAsk(priceLevel));
    }

    private void addB(Instant bucket, PriceLevel priceLevel) {
        ticks.computeIfAbsent(bucket, Tick::new);
        ticks.computeIfPresent(bucket, (b, tick) -> tick.addBid(priceLevel));
    }
}
