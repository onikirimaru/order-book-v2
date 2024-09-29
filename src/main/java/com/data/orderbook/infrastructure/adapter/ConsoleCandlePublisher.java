package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.ports.out.CandlePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "order-book.output", havingValue = "console", matchIfMissing = true)
public class ConsoleCandlePublisher implements CandlePublisher {

    @Override
    public void publish(String pair, OrderBookCandle value) {
        System.out.println("___________________");
        System.out.println("___________________");
    }
}
