package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.OrderBookUpdate;

public class OrderBookUpdateFixture {

    public static OrderBookUpdate create() {
        return new OrderBookUpdate("PAIR/PAIR", "1", null, PriceLevelFixture.createListOfTwo(), null);
    }
}
