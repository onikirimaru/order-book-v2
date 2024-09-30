package com.data.orderbook.domain.ports.out;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CandlePublisherPort {

    void publish(OrderBookCandle value) throws JsonProcessingException;
}
