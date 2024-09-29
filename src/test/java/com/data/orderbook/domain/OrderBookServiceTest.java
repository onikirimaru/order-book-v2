package com.data.orderbook.domain;

import com.data.orderbook.domain.fixtures.OrderBookUpdateFixture;
import org.junit.jupiter.api.Test;

class OrderBookServiceTest {

    OrderBookService orderBookService = new OrderBookService();

    @Test
    void shouldIngest() {
        var result = orderBookService.ingest(OrderBookUpdateFixture.create());
    }
}