package com.data.orderbook.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.data.orderbook.domain.fixtures.OrderBookUpdateFixture;
import com.data.orderbook.domain.fixtures.TickFixture;
import com.data.orderbook.domain.model.ClockProviderMock;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OrderBookServiceTest {

    public static final Instant CANDLE_INSTANT = Instant.ofEpochSecond(1727348270L);
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
            assertThat(orderBook.lastUpdate()).isEqualTo(Instant.MIN);
            var expectedTick = TickFixture.createFirstTick();
            assertThat(orderBook.ticks())
                    .isEqualTo(Map.of(Instant.ofEpochSecond(TickFixture.BUCKET_EPOCH_SECOND), expectedTick));
        });
    }

    @Test
    void shouldIngestFirstTick() {
        orderBookService.createBook("PAIR/PAIR");
        var result = orderBookService.ingest(OrderBookUpdateFixture.create());
        assertThat(result).satisfies(orderBook -> {
            assertThat(orderBook.lastUpdate())
                    .isEqualTo(Instant.ofEpochSecond(CANDLE_INSTANT.getEpochSecond(), 841901000L));
            var expectedTick = TickFixture.createFirstTickWithTwoElements();
            assertThat(orderBook.ticks())
                    .isEqualTo(Map.of(Instant.ofEpochSecond(TickFixture.BUCKET_EPOCH_SECOND), expectedTick));
        });
    }
}
