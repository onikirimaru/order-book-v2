package com.data.orderbook.domain;

import com.data.orderbook.domain.ports.in.OrderBookService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BaseOrderBookService implements OrderBookService {

    private Map<String, OrderBook> books;

    public void update() {}

    @Override
    public void createBook(String pair) {
        books.putIfAbsent(pair, new OrderBook(pair));
    }

    @Override
    public void ingest(OrderBookUpdate update) {}

    @Override
    public void ingest(OrderBookSnapshot snapshot) {}

    public List<OrderBookCandle> candles(Instant minute) {
        // Span a thread to perform
        return List.of();
    }
}
