package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.ports.out.CandlePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(value = "order-book.output", havingValue = "console", matchIfMissing = true)
public class ConsoleCandlePublisher implements CandlePublisher {
    @Override
    public void publish(Map<String, OrderBookCandle> candles) {
        System.out.println("-------------------");
        System.out.println("___________________");

    }
}
