package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.FirstTick;
import com.data.orderbook.domain.model.Tick;
import java.time.Instant;

public class TickFixture {

    public static final long BUCKET_EPOCH_SECOND = 1727348280L;

    public static Tick create() {
        var tick = new Tick(Instant.ofEpochSecond(1727348280L), null);
        return tick.addAsk(PriceLevelUpdateFixture.create());
    }

    public static Tick createListOfTwo() {
        var tick = new Tick(Instant.ofEpochSecond(1727348280L), null);
        PriceLevelUpdateFixture.createListOfTwo().forEach(tick::addAsk);
        PriceLevelUpdateFixture.createListOfTwo().forEach(tick::addBid);
        tick.incrementTotalUpdates();
        return tick;
    }

    public static Tick createListOfTwoWitPrevious() {
        var previousTick = createFirstTickWithTwoElements(BUCKET_EPOCH_SECOND - 60);
        var tick = new Tick(Instant.ofEpochSecond(1727348280L), previousTick);
        PriceLevelUpdateFixture.createListOfTwo().forEach(tick::addAsk);
        PriceLevelUpdateFixture.createListOfTwo().forEach(tick::addBid);
        tick.incrementTotalUpdates();
        return tick;
    }

    public static FirstTick createFirstTickWithTwoElements() {
        return createFirstTickWithTwoElements(BUCKET_EPOCH_SECOND);
    }

    public static FirstTick createFirstTick() {
        return new FirstTick(Instant.ofEpochSecond(BUCKET_EPOCH_SECOND));
    }

    public static FirstTick createFirstTickWithTwoElements(long bucketEpochSecond) {
        var firstTick = new FirstTick(Instant.ofEpochSecond(bucketEpochSecond));
        PriceLevelUpdateFixture.createListOfTwo(bucketEpochSecond).forEach(firstTick::addAsk);
        PriceLevelUpdateFixture.createListOfTwo(bucketEpochSecond).forEach(firstTick::addBid);
        firstTick.incrementTotalUpdates();
        return firstTick;
    }
}
