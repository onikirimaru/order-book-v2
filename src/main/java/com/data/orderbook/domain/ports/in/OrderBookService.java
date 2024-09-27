package com.data.orderbook.domain.ports.in;

import com.data.orderbook.domain.OrderBookUpdate;

public interface OrderBookService {

    void createBook(String pair);

    void ingest(OrderBookUpdate map);
}
