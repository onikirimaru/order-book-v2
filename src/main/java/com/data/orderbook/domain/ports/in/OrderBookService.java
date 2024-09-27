package com.data.orderbook.domain.ports.in;

import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;

import java.time.Instant;
import java.util.Map;

public interface OrderBookService {

    void createBook(String pair);

    void ingest(OrderBookUpdate update);

    void ingest(OrderBookSnapshot snapshot);

    Map<String, OrderBook> fetch(Instant time);
}
