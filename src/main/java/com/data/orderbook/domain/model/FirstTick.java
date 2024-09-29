package com.data.orderbook.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class FirstTick extends Tick {

    public static final PriceLevel STARTING_PRICELEVEL = new PriceLevel(BigDecimal.ZERO, BigDecimal.ZERO, Instant.MIN);

    public FirstTick(Instant bucket) {
        super(bucket, null);
    }

    @Override
    public PriceLevel startingA() {
        return STARTING_PRICELEVEL;
    }

    @Override
    public PriceLevel startingB() {
        return STARTING_PRICELEVEL;
    }
}
