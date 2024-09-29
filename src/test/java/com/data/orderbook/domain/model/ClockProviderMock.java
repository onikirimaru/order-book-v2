package com.data.orderbook.domain.model;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class ClockProviderMock extends ClockProvider {

    private final Clock clock;

    public ClockProviderMock(Instant fixedInstant) {
        this.clock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
    }

    @Override
    public Clock clock() {
        return this.clock;
    }
}
