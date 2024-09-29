package com.data.orderbook.domain.model;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
public class Tick {
    private final Instant bucket;
    private final List<PriceLevel> as = new LinkedList<>();
    private final List<PriceLevel> bs = new LinkedList<>();

    @Getter
    private final PriceLevel startingA;

    @Getter
    private final PriceLevel startingB;

    @Getter
    private PriceLevel lastA;

    @Getter
    private PriceLevel lastB;

    public Tick(Instant bucket, Tick previousTick) {
        this.bucket = bucket;
        this.startingA = Optional.ofNullable(previousTick).map(p -> p.lastA).orElse(null);
        this.startingB = Optional.ofNullable(previousTick).map(p -> p.lastB).orElse(null);
    }

    public Tick addAsk(PriceLevel a) {
        this.as.add(a);
        this.lastA = a;
        return this;
    }

    public Tick addBid(PriceLevel b) {
        this.bs.add(b);
        this.lastB = b;
        return this;
    }
}
