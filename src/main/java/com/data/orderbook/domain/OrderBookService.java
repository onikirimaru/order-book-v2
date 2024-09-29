package com.data.orderbook.domain;

import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class OrderBookService implements OrderBookServicePort {

    private final Map<String, OrderBook> books;

    public OrderBookService() {
        this.books = new ConcurrentHashMap<>();
    }

    @Override
    public void createBook(String pair) {
        books.putIfAbsent(pair, new OrderBook(pair));
    }

    @Override
    public OrderBook ingest(OrderBookUpdate update) {
        return books.computeIfPresent(update.pair(), (k, v) -> v.ingest(update));
    }

    @Override
    public void ingest(OrderBookSnapshot snapshot) {}

    @Override
    public Map<String, OrderBook> calculateCandle(Instant time) {
        return Map.of();
    }

    public List<OrderBookCandle> candles(Instant minute) {
        // Span a thread to perform
        return List.of();
    }
}
