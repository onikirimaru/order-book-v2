package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "order-book.candle.output", havingValue = "console", matchIfMissing = true)
public class ConsoleCandlePublisher implements CandlePublisherPort {

    private final ObjectMapper objectMapper;

    public ConsoleCandlePublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(OrderBookCandle candle) {
        var candleAsJson = Try.of(() -> objectMapper.writeValueAsString(candle)).get();
        System.out.println(
                "_______________________________________________________________________________________________");
        System.out.println("Instant:" + candle.timestamp());
        System.out.println("Pair:" + candle.pair());
        System.out.println(candleAsJson);
        System.out.println(
                "_______________________________________________________________________________________________");
    }
}
