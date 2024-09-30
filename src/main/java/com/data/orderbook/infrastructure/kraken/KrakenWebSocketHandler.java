package com.data.orderbook.infrastructure.kraken;

import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import com.data.orderbook.infrastructure.kraken.domain.MessageMapper;
import com.data.orderbook.infrastructure.kraken.domain.StartEvent;
import com.data.orderbook.infrastructure.kraken.domain.SubscriptionEvent;
import com.data.orderbook.infrastructure.kraken.domain.mapper.SnapshotMessageMapper;
import com.data.orderbook.infrastructure.kraken.domain.mapper.UpdateMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Service
public class KrakenWebSocketHandler extends TextWebSocketHandler {

    public static final String EVENT_HEARTBEAT = "{\"event\":\"heartbeat\"}";
    private final List<String> pairs;
    private final UpdateMessageMapper updateMessageMapper;
    private final SnapshotMessageMapper snapshotMessageMapper;
    private final OrderBookServicePort orderBookService;
    private final MessageMapper messageMapper;

    private Status status = Status.STARTING;

    public KrakenWebSocketHandler(
            @Value("#{'${order-book.pairs}'.split(',')}") List<String> pairs,
            ObjectMapper objectMapper,
            SnapshotMessageMapper snapshotMessageMapper,
            MessageMapper messageMapper,
            UpdateMessageMapper updateMessageMapper,
            OrderBookServicePort orderBookService) {
        this.pairs = pairs;
        this.updateMessageMapper = updateMessageMapper;
        this.snapshotMessageMapper = snapshotMessageMapper;
        this.orderBookService = orderBookService;
        this.messageMapper = messageMapper;

        pairs.forEach(orderBookService::createBook);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        switch (status) {
            case STARTING -> handleConnection(session, message);
            case SUBSCRIPTION_NOT_CONFIRMED -> handleSubscriptionPending(session, message);
            case SUBSCRIBED -> handlingSnapshot(session, message);
            case PROCESSING -> handleUpdateMessage(session, message);
            default -> log.warn("Status '{} not handled'", status);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.exit(status.getCode());
    }

    private void handleUpdateMessage(WebSocketSession session, TextMessage message) {
        if (message.getPayload().equals(EVENT_HEARTBEAT)) {
            // Heart beat received
            log.debug("Heartbeat received");
            return;
        }
        log.debug("Update received: '{}'", message.getPayload());
        OrderBookUpdate mapped = updateMessageMapper.map(message);
        orderBookService.ingest(mapped);
    }

    private void handlingSnapshot(WebSocketSession session, TextMessage message) {
        if (message.getPayload().equals(EVENT_HEARTBEAT)) {
            // Heart beat received
            log.debug("Heartbeat received");
            return;
        }
        log.info("Snapshot received: '{}'", message.getPayload());
        final var map = snapshotMessageMapper.map(message);
        orderBookService.ingest(map);
        status = Status.PROCESSING;
    }

    private void handleSubscriptionPending(WebSocketSession session, TextMessage message) {
        if (message.getPayload().contains("errorMessage")) {
            log.error("Error handling subscription '{}'", message.getPayload());
            // FIXME need to handle existing application in proper handler
            System.exit(-1);
        }
        log.info("Subscribed to '{}'", message.getPayload());
        status = Status.SUBSCRIBED;
    }

    private void handleConnection(WebSocketSession session, TextMessage message) {
        // FIXME Need to move this deserialisation to proper mappers
        final var startEvent = messageMapper.<StartEvent>deserialise(message);
        log.info("Connection started: {}", startEvent.get());
        status = Status.ONLINE;
        final var subscriptionMessage = messageMapper.serialize(SubscriptionEvent.book(pairs, 1));
        log.info("Subscription message: '{}'", new String(subscriptionMessage.get()));
        // FIXME Handle the error an terminate the application
        Try.run(() -> session.sendMessage(new TextMessage(subscriptionMessage.get())));
        status = Status.SUBSCRIPTION_NOT_CONFIRMED;
    }

    private enum Status {
        STARTING,
        ONLINE,
        SUBSCRIPTION_NOT_CONFIRMED,
        SUBSCRIBED,
        PROCESSING;
    }
}
