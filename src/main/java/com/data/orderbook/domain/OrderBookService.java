package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderBookService implements OrderBookServicePort {

    private final ClockProvider clockProvider;
    private Instant instant;

    private final Map<String, OrderBook> books;

    public OrderBookService(ClockProvider clockProvider) {
        this.books = new ConcurrentHashMap<>();
        this.clockProvider = clockProvider;
    }

    @Override
    public void createBook(String pair) {
        this.instant = Instant.now(clockProvider.clock());
        books.putIfAbsent(pair, new OrderBook(pair, clockProvider));
    }

    @Override
    public OrderBook ingest(OrderBookUpdate update) {
        // Books should always exist
        var pair = update.pair();
        books.computeIfAbsent(pair, p -> {
            log.warn("Pair '{}' not configured, creating new Order Book", p);
            return new OrderBook(p, clockProvider);
        });
        return books.computeIfPresent(pair, (k, v) -> v.ingest(update));
    }

    @Override
    public void ingest(OrderBookSnapshot snapshot) {
    }

    @Override
    public Map<String, OrderBookCandle> calculateCandle(Instant time) {
        return Map.of();
    }

    public List<OrderBookCandle> candles(Instant minute) {
        // Span a thread to perform
        return List.of();
    }
}
