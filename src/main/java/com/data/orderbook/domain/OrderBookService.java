package com.data.orderbook.domain;

import com.data.orderbook.domain.model.Candle;
import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.model.OrderBook;
import com.data.orderbook.domain.model.OrderBookSnapshot;
import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import io.vavr.Tuple2;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderBookService implements OrderBookServicePort {

    private final ClockProvider clockProvider;
    private final RoundingMode roundingMode;
    private Instant instant;
    private final Integer depth;

    private final Map<String, OrderBook> books;

    public OrderBookService(
            @Value("${order-book.depth}") Integer depth,
            ClockProvider clockProvider,
            @Value("${order-book.candle.rounding-mode}") RoundingMode roundingMode) {
        this.books = new ConcurrentHashMap<>();
        this.clockProvider = clockProvider;
        this.depth = depth;
        this.roundingMode = roundingMode;
    }

    @Override
    public void createBook(String pair) {
        this.instant = Instant.now(clockProvider.clock());
        books.putIfAbsent(pair, new OrderBook(depth, pair, clockProvider, roundingMode));
    }

    @Override
    public OrderBook ingest(OrderBookUpdate update) {
        // Books should always exist
        this.instant = Instant.now(clockProvider.clock());
        var pair = update.pair();
        validateAndSetupPair(pair);
        return books.computeIfPresent(pair, (k, v) -> v.ingest(update));
    }

    @Override
    public OrderBook ingest(OrderBookSnapshot snapshot) {
        this.instant = Instant.now(clockProvider.clock());
        var pair = snapshot.pair();
        validateAndSetupPair(pair);
        return books.computeIfPresent(pair, (k, v) -> v.ingest(snapshot));
    }

    @Override
    public Map<String, Candle> getCandle(Instant time) {
        return books.entrySet().stream()
                .map(booksEntry -> {
                    final var candle = booksEntry.getValue().fetchCandle(time);
                    return new Tuple2<>(booksEntry.getKey(), candle);
                })
                .collect(Collectors.toMap(t -> t._1, t -> t._2));
    }

    private void validateAndSetupPair(String pair) {
        books.computeIfAbsent(pair, p -> {
            log.warn("Pair '{}' not configured, creating new Order Book", p);
            return new OrderBook(depth, p, clockProvider, roundingMode);
        });
    }
}
