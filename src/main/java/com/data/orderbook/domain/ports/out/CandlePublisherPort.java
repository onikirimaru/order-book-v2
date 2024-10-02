package com.data.orderbook.domain.ports.out;

import com.data.orderbook.domain.model.Candle;

public interface CandlePublisherPort {

    void publish(Candle candle);
}
