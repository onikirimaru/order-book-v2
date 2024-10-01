package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "order-book.candle.output", havingValue = "kafka", matchIfMissing = true)
public class KafkaCandlePublisher implements CandlePublisherPort {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaCandlePublisher(
            @Value("${order-book.kafka.producers.candle.topic}") String topic,
            KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(OrderBookCandle candle) {
        kafkaTemplate.send(topic, candle.pair(), candle).join();
    }
}
