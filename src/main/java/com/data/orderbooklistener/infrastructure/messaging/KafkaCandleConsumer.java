package com.data.orderbooklistener.infrastructure.messaging;

import com.data.orderbooklistener.infrastructure.messaging.domain.model.CandleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "order-book.candle.output", havingValue = "kafka")
@Slf4j
public class KafkaCandleConsumer {

    @KafkaListener(
            groupId = "${order-book.kafka.consumers.group-id}",
            topics = "${order-book.kafka.consumers.candle.topic}",
            clientIdPrefix = "${order-book.kafka.consumers.client-id-prefix}")
    void update(final CandleEvent event) {
        log.info("Consuming event: '{}'", event);
    }
}
