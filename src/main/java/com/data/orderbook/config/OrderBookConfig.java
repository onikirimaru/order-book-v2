package com.data.orderbook.config;

import com.data.orderbook.infrastructure.kraken.OrderBookHandler;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.Extension;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Slf4j
@Configuration
@EnableWebSocket
public class OrderBookConfig {

    private final String wssURI;

    public OrderBookConfig(@Value("${order-book.wssURI}") String wssURI) {
        this.wssURI = wssURI;
    }

    @Bean
    public StandardWebSocketClient client() {
        final var container = new MyWebSocketContainer();
        return new StandardWebSocketClient();
    }

    @Bean
    public StompSessionHandler sessionHandler() {
        return new MyStompSessionHandler();
    }

    @Bean
    public WebSocketSession stompSession(
            StandardWebSocketClient client, OrderBookHandler handler, StompSessionHandler sessionHandler) {
        return client.execute(handler, wssURI).join();
    }

    private static class MyStompSessionHandler implements StompSessionHandler {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Connected");
        }

        @Override
        public void handleException(
                StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.info("Exception");
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.info("Transport");
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            log.info("Payload");
            return null;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.info("Frame");
        }
    }

    private static class MyWebSocketContainer implements WebSocketContainer {

        @Override
        public long getDefaultAsyncSendTimeout() {
            return 10;
        }

        @Override
        public void setAsyncSendTimeout(long timeout) {}

        @Override
        public Session connectToServer(Object endpoint, URI path) throws DeploymentException, IOException {
            return null;
        }

        @Override
        public Session connectToServer(Class<?> annotatedEndpointClass, URI path)
                throws DeploymentException, IOException {
            return null;
        }

        @Override
        public Session connectToServer(Endpoint endpoint, ClientEndpointConfig clientEndpointConfiguration, URI path)
                throws DeploymentException, IOException {
            return null;
        }

        @Override
        public Session connectToServer(
                Class<? extends Endpoint> endpoint, ClientEndpointConfig clientEndpointConfiguration, URI path)
                throws DeploymentException, IOException {
            return null;
        }

        @Override
        public long getDefaultMaxSessionIdleTimeout() {
            return 0;
        }

        @Override
        public void setDefaultMaxSessionIdleTimeout(long timeout) {}

        @Override
        public int getDefaultMaxBinaryMessageBufferSize() {
            return 0;
        }

        @Override
        public void setDefaultMaxBinaryMessageBufferSize(int max) {}

        @Override
        public int getDefaultMaxTextMessageBufferSize() {
            return 0;
        }

        @Override
        public void setDefaultMaxTextMessageBufferSize(int max) {}

        @Override
        public Set<Extension> getInstalledExtensions() {
            return Set.of();
        }
    }
}
