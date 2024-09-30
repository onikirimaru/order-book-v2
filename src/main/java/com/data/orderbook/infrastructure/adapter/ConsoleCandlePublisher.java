package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void publish(OrderBookCandle candle) throws JsonProcessingException {
        System.out.println(
                "_______________________________________________________________________________________________");
        System.out.println("Instant:" + candle.timestamp());
        System.out.println("Pair:" + candle.pair());
        System.out.println(objectMapper.writeValueAsString(candle));
        System.out.println(
                "_______________________________________________________________________________________________");
    }
}
