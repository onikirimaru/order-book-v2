package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.model.Tick;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import io.vavr.Tuple2;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderBookService implements OrderBookServicePort {

    private final ClockProvider clockProvider;
    private Instant instant;
    private final Integer depth;

    private final Map<String, OrderBook> books;

    public OrderBookService(@Value("${order-book.depth}") Integer depth, ClockProvider clockProvider) {
        this.books = new ConcurrentHashMap<>();
        this.clockProvider = clockProvider;
        this.depth = depth;
    }

    @Override
    public void createBook(String pair) {
        this.instant = Instant.now(clockProvider.clock());
        books.putIfAbsent(pair, new OrderBook(depth, pair, clockProvider));
    }

    @Override
    public OrderBook ingest(OrderBookUpdate update) {
        // Books should always exist
        var pair = update.pair();
        books.computeIfAbsent(pair, p -> {
            log.warn("Pair '{}' not configured, creating new Order Book", p);
            return new OrderBook(depth, p, clockProvider);
        });
        return books.computeIfPresent(pair, (k, v) -> v.ingest(update));
    }

    @Override
    public void ingest(OrderBookSnapshot snapshot) {
        // Pending
    }

    @Override
    public Map<String, Optional<Tick>> ticks(Instant time) {
        return books.entrySet().stream()
                .map(booksEntry -> {
                    Optional<Tick> tick = booksEntry.getValue().fetchTick(time);
                    var tickTuple = new Tuple2<>(booksEntry.getKey(), tick);
                    tick.ifPresent(t -> booksEntry.getValue().remove(time));
                    return tickTuple;
                })
                .collect(Collectors.toMap(t -> t._1, t -> t._2));
    }
}
