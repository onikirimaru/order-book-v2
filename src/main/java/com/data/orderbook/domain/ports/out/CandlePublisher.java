package com.data.orderbook.domain.ports.out;

import com.data.orderbook.domain.model.OrderBookCandle;

public interface CandlePublisher {

    void publish(OrderBookCandle candles);

    void publish(String key, OrderBookCandle value);
}
