package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.Tick;
import java.time.Instant;

public class TickFixture {

    public static Tick create() {
        var tick = new Tick(Instant.ofEpochSecond(1727348280L));
        return tick.addAsk(PriceLevelFixture.create());
    }

    public static Tick createListOfTwo() {
        var tick = new Tick(Instant.ofEpochSecond(1727348280L));
        PriceLevelFixture.createListOfTwo().forEach(tick::addAsk);
        PriceLevelFixture.createListOfTwo().forEach(tick::addBid);
        return tick;
    }
}
