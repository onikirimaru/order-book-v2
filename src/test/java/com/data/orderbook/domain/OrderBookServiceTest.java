package com.data.orderbook.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.data.orderbook.domain.fixtures.OrderBookUpdateFixture;
import com.data.orderbook.domain.fixtures.TickFixture;
import com.data.orderbook.domain.model.ClockProviderMock;
import com.data.orderbook.domain.model.FirstTick;
import java.math.RoundingMode;
import java.time.Instant;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class OrderBookServiceTest {

    public static final Instant CANDLE_INSTANT = Instant.ofEpochSecond(1727348270L);
    OrderBookService orderBookService =
            new OrderBookService(10, new ClockProviderMock(CANDLE_INSTANT), RoundingMode.CEILING);

    @Test
    void shouldIngestWhenBookDoesNotExist() {
        var result = orderBookService.ingest(OrderBookUpdateFixture.create());
        assertThat(result).satisfies(ob -> {
            assertThat(ob.mutableAsks().size()).isEqualTo(1);
            assertThat(ob.mutableAsks()).isInstanceOf(FirstTick.class);
        });
    }

    @Test
    void shouldIngestWhenEmptyTicks() {
        orderBookService.createBook("PAIR/PAIR");
        var result = orderBookService.ingest(OrderBookUpdateFixture.createEmpty());
        assertThat(result).satisfies(orderBook -> {
            assertThat(orderBook.lastUpdate()).isEqualTo(Instant.MIN);
            var expectedTick = TickFixture.createFirstTick();
            expectedTick.incrementTotalUpdates(); // We ingest an empty update
            assertThat(orderBook.mutableAsks()).isEqualTo(expectedTick);
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
            assertThat(orderBook.mutableAsks()).isEqualTo(expectedTick);
        });
    }
}
