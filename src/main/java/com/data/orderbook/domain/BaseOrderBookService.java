package com.data.orderbook.domain;

import com.data.orderbook.domain.ports.OrderBookService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BaseOrderBookService implements OrderBookService {

    private Map<String, OrderBook> books;

    public void update() {}

    @Override
    public void createBook(String pair) {
    }

    public List<OrderBookCandle> candles() {
        //Span a thread to perform
        return List.of();
    }
}
