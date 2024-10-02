package com.data.orderbook.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FirstTick extends Tick {

    public static final PriceLevelUpdate STARTING_PRICELEVEL = new PriceLevelUpdate(BigDecimal.ZERO, BigDecimal.ZERO, Instant.MIN, null);

    public FirstTick(Instant bucket) {
        super(bucket, null);
    }

    @Override
    public PriceLevelUpdate startingA() {
        return STARTING_PRICELEVEL;
    }

    @Override
    public PriceLevelUpdate startingB() {
        return STARTING_PRICELEVEL;
    }
}
