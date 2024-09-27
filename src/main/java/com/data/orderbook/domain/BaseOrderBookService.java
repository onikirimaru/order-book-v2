package com.data.orderbook.domain;

import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.ports.in.OrderBookService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BaseOrderBookService implements OrderBookService {

    private final Map<String, OrderBook> books;

    public BaseOrderBookService() {
        this.books = new HashMap<>();
    }

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
