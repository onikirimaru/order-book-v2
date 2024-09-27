package com.data.orderbook.domain.ports.out;

import com.data.orderbook.domain.model.OrderBookCandle;

import java.util.Map;

public interface CandlePublisher {

    void publish(Map<String, OrderBookCandle> candles);
}
