package com.data.orderbook.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final long SECONDS = TimeUnit.MINUTES.toSeconds(1);
    private static final BigDecimal ZERO = new BigDecimal("0.00000000");
    private static final BigDecimal LOWEST_ASK_BY_DEFAULT = new BigDecimal("10000000000");
    private final Instant instant;
    private final String pair;
    private final Integer depth;
    private final ClockProvider clockProvider;
    private final RoundingMode roundingMode;
    // FIXME We need a map to index the price levels and make searchs faster
    private List<PriceLevel> mutableAsks = new LinkedList<>();
    private List<PriceLevel> mutableBids = new LinkedList<>();
    private BigDecimal open;
    private BigDecimal last;
    private BigDecimal high;
    private BigDecimal low;
    private Instant lastUpdate = Instant.MIN;
    private final AtomicInteger totalUpdates;

    public OrderBook(Integer depth, String pair, ClockProvider clockProvider, RoundingMode roundingMode) {
        this.clockProvider = clockProvider;
        this.pair = pair;
        this.depth = depth;
        this.roundingMode = roundingMode;
        this.instant = Instant.now(clockProvider.clock());
        this.totalUpdates = new AtomicInteger(0);
    }

    public OrderBook ingest(OrderBookSnapshot snapshot) {
        refreshAll(this.mutableAsks, snapshot.as());
        refreshAll(this.mutableBids, snapshot.bs());
        lastUpdate = Instant.now(clockProvider.clock());
        refreshCandle();
        return this;
    }

    public OrderBook ingest(OrderBookUpdate update) {
        lastUpdate = Instant.now(clockProvider.clock());
        mutableAsks = Optional.ofNullable(update.a())
                .map(asksUpdate -> asksUpdate.stream()
                        .reduce(this.mutableAsks, (ma, au) -> ingest(au, ma, Type.ASK), (ma1, ma2) -> ma1))
                .orElse(this.mutableAsks);
        mutableBids = Optional.ofNullable(update.b())
                .map(asksUpdate -> asksUpdate.stream()
                        .reduce(this.mutableBids, (pls, plu) -> ingest(plu, pls, Type.BID), (pls, pls_) -> pls))
                .orElse(this.mutableBids);
        normalise();
        refreshCandle();
        this.totalUpdates.addAndGet(1);
        return this;
    }

    private List<PriceLevel> ingest(PriceLevelUpdate plu, List<PriceLevel> priceLevels, Type type) {
        // According to
        // https://support.kraken.com/hc/en-us/articles/360027821131-WebSocket-API-v1-How-to-maintain-a-valid-order-book
        var updatedPls = priceLevels.stream()
                .filter(pl -> !pl.price().equals(plu.price()))
                .toList();
        // Remove entry if 0.0
        if (plu.volume().equals(ZERO)) {
            if (updatedPls.size() == priceLevels.size()) {
                log.warn("{} not found for update '{}'", type, plu);
            }
            return updatedPls;
        }
        // Entry was already removed if existed
        // Add new entry;
        return Stream.concat(
                updatedPls.stream(),
                Stream.of(new
                        PriceLevel(plu.price(), plu.
                        volume(), plu.
                        timestamp()))
        ).toList();
    }

    private void normalise() {
        this.mutableAsks = mutableAsks.stream()
                .sorted(Comparator.comparing(PriceLevel::price))
                .toList()
                .subList(0, depth);
        this.mutableBids = mutableBids.stream()
                .sorted(Comparator.comparing(PriceLevel::price).reversed())
                .toList()
                .subList(0, depth);
    }

    private void refreshCandle() {
        var mp = midPrice();
        if (totalUpdates.get() == 0) {
            open = mp;
            high = mp;
            low = mp;
            last = mp;
            return;
        }
        high = mp.compareTo(high) > 0 ? midPrice() : high;
        low = mp.compareTo(last) < 0 ? midPrice() : last;
        last = mp;
    }

    public Candle fetchCandle(Instant instant) {
        log.info("Fetching '{}' '{}' tick with '{}' updates", pair, instant.getEpochSecond(), totalUpdates.get());
        final var candle = new Candle(
                UUID.randomUUID().toString(),
                pair,
                instant.getEpochSecond(),
                open,
                high,
                low,
                last,
                totalUpdates.get());
        reset();
        return candle;
    }

    private void reset() {
        totalUpdates.set(0);
        open = null;
        last = null;
        high = null;
        low = null;
    }

    private void refreshAll(List<PriceLevel> priceLevels, List<PriceLevelUpdate> snapshotUpdates) {
        var snapshotLevels = snapshotUpdates.stream()
                .map(plu -> new PriceLevel(plu.price(), plu.volume(), plu.timestamp()))
                .toList();
        priceLevels.clear();
        priceLevels.addAll(snapshotLevels);
    }

    private BigDecimal midPrice() {
        var hb = highestBid();
        var la = lowestAsk();
        return hb.add(la).divide(BigDecimal.TWO, roundingMode);
    }

    private BigDecimal lowestAsk() {
        return mutableAsks.stream().map(PriceLevel::price).min(BigDecimal::compareTo).orElse(LOWEST_ASK_BY_DEFAULT);
    }

    private BigDecimal highestBid() {
        return mutableBids.stream().map(PriceLevel::price).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public enum Type {
        BID,
        ASK
    }
}
