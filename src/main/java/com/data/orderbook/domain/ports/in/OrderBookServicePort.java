package com.data.orderbook.domain.ports.in;

import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.model.Tick;
import java.time.Instant;
import java.util.Map;

public interface OrderBookServicePort {

    void createBook(String pair);

    OrderBook ingest(OrderBookUpdate update);

    void ingest(OrderBookSnapshot snapshot);

    Map<String, Tick> ticks(Instant time);
}
