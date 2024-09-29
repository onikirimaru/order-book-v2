package com.data.orderbook.domain.ports.out;

import com.data.orderbook.domain.model.OrderBookCandle;

public interface CandlePublisherPort {

    void publish(OrderBookCandle value);
}
