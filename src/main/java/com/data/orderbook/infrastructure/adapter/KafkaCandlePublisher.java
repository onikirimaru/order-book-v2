package com.data.orderbook.infrastructure.adapter;

import com.data.orderbook.domain.model.Candle;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "order-book.candle.output", havingValue = "kafka", matchIfMissing = true)
public class KafkaCandlePublisher implements CandlePublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaCandlePublisher(
            @Value("${order-book.kafka.producers.candle.topic}") String topic,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(Candle candle) {
        kafkaTemplate.send(topic, candle.pair(), candle).join();
    }
}
