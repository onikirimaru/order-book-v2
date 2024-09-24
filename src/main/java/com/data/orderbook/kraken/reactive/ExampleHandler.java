package com.data.orderbook.kraken.reactive;

import java.util.List;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExampleHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Flux<WebSocketMessage> output = session.receive()
                .doOnNext(System.out::println)
                .concatMap(message -> {
                    return Flux.fromIterable(List.of(message));
                })
                .map(value -> session.textMessage("Echo " + value));

        return session.send(output);
    }
}
