package com.data.orderbook.config;

import com.data.orderbook.infrastructure.kraken.KrakenWebSocketHandler;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.Extension;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

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
    public WebSocketSession stompSession(
            StandardWebSocketClient client, KrakenWebSocketHandler handler) {
        return client.execute(handler, wssURI).join();
    }

    private static class MyWebSocketContainer implements WebSocketContainer {

        @Override
        public long getDefaultAsyncSendTimeout() {
            return 10;
        }

        @Override
        public void setAsyncSendTimeout(long timeout) {}

        @Override
        public Session connectToServer(Object endpoint, URI path) {
            return null;
        }

        @Override
        public Session connectToServer(Class<?> annotatedEndpointClass, URI path) {
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
