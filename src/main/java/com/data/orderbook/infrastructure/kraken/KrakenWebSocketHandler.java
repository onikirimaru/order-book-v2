package com.data.orderbook.infrastructure.kraken;

import com.data.orderbook.domain.BaseOrderBookService;
import com.data.orderbook.infrastructure.kraken.domain.StartEvent;
import com.data.orderbook.infrastructure.kraken.domain.SubscriptionEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class KrakenWebSocketHandler extends TextWebSocketHandler {

    public static final String EVENT_HEARTBEAT = "{\"event\":\"heartbeat\"}";
    private final ObjectMapper objectMapper;
    private final List<String> pairs;

    private Status status = Status.STARTING;

    public KrakenWebSocketHandler(
            @Value("#{'${order-book.pairs}'.split(',')}") List<String> pairs,
            ObjectMapper objectMapper,
            BaseOrderBookService orderBookService
    ) {
        this.objectMapper = objectMapper;
        this.pairs = pairs;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        switch (status) {
            case STARTING -> {
                final var startEvent = this.<StartEvent>deserialise(message);
                log.info("Connection started: {}", startEvent.get());
                status = Status.ONLINE;
                final var subscriptionMessage = serialize(SubscriptionEvent.book(pairs, 1));
                log.info("Subscription message: '{}'", new String(subscriptionMessage.get()));
                //FIXME Handle the error an terminate the application
                Try.run(() -> session.sendMessage(new TextMessage(subscriptionMessage.get())));
                status = Status.SUBSCRIBED;
            }
            case SUBSCRIPTION_NOT_CONFIRMED -> {
                log.info("Subscribed to '{}'", message.getPayload());
                status = Status.SUBSCRIBED;
            }
            case SUBSCRIBED -> {
                log.debug("Update received: '{}'", message.getPayload());
                if (message.getPayload().equals(EVENT_HEARTBEAT)) {
                    //Heart beat received
                    return;
                }
                final var subscriptionMessage = this.<List<String>>deserialise(message);
            }
            default -> log.warn("Status '{} not handled'", status);
        }
    }

    private <T> Try<byte[]> serialize(T object) {
        return Try.of(() -> objectMapper.writeValueAsBytes(object));
    }

    private <T> Try<T> deserialise(TextMessage message) {
        return Try.of(() ->
                objectMapper.readValue(message.getPayload().getBytes(StandardCharsets.UTF_8), new TypeReference<>() {
                }));
    }

    private enum Status {
        STARTING,
        ONLINE,
        SUBSCRIPTION_NOT_CONFIRMED,
        SUBSCRIBED,
        PROCESSING;
    }
}
