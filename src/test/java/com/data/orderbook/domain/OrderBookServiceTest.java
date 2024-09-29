package com.data.orderbook.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.data.orderbook.domain.fixtures.OrderBookUpdateFixture;
import com.data.orderbook.domain.fixtures.TickFixture;
import com.data.orderbook.domain.model.ClockProviderMock;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OrderBookServiceTest {

    public static final Instant CANDLE_INSTANT = Instant.ofEpochSecond(1727348280L);
    OrderBookService orderBookService = new OrderBookService(new ClockProviderMock(CANDLE_INSTANT));

    @Test
    void shouldIngestWhenBookDoesNotExist() {
        var result = orderBookService.ingest(OrderBookUpdateFixture.create());
        assertThat(result).isNull();
    }

    @Test
    void shouldIngestWhenEmptyTicks() {
        orderBookService.createBook("PAIR/PAIR");
        var result = orderBookService.ingest(OrderBookUpdateFixture.createEmpty());
        assertThat(result).satisfies(orderBook -> {
            assertThat(orderBook.lastUpdate()).isNull();
            assertThat(orderBook.ticks()).isEqualTo(Map.of());
        });
    }

    @Test
    void shouldIngest() {
        orderBookService.createBook("PAIR/PAIR");
        var result = orderBookService.ingest(OrderBookUpdateFixture.create());
        assertThat(result).satisfies(orderBook -> {
            assertThat(orderBook.lastUpdate()).isEqualTo(Instant.ofEpochSecond(1727348278L, 841901000L));
            assertThat(orderBook.ticks()).isEqualTo(Map.of(CANDLE_INSTANT, TickFixture.createListOfTwo()));
        });
    }
}
