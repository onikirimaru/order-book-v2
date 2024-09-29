package com.data.orderbook.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter(onMethod = @__(@JsonProperty))
@Accessors(fluent = true)
@ToString
public class OrderBook {

    private final Instant instant = Instant.now();
    private final String pair;
    private final ConcurrentHashMap<Instant, List<Tick>> ticks = new ConcurrentHashMap<>();
    private Instant lastUpdate;

    public OrderBook(String pair) {
        this.pair = pair;
    }

    public OrderBook ingest(OrderBookUpdate update) {
        return this;
    }
}
