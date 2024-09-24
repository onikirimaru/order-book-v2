package com.data.orderbook.infrastructure.kraken;

import com.data.orderbook.infrastructure.kraken.domain.StartEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class OrderBookHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    public OrderBookHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        final var startEvent = Try.of(() -> objectMapper.readValue(message.getPayload().getBytes(StandardCharsets.UTF_8), StartEvent.class));
        log.info("Connection started: {}", startEvent.get());
    }
}
