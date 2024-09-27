package com.data.orderbook.domain;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderBook {

    private final String pair;
    private final ConcurrentHashMap<Instant, List<Ticks>> ticks = new ConcurrentHashMap<>();
}
