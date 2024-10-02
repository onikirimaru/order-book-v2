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

    @Getter
    private final List<PriceLevelUpdate> as = new LinkedList<>();

    @Getter
    private final List<PriceLevelUpdate> bs = new LinkedList<>();

    private Integer numberOfUpdates = 0;

    @Getter
    private final PriceLevelUpdate startingA;

    @Getter
    private final PriceLevelUpdate startingB;

    @Getter
    private PriceLevelUpdate lastA;

    @Getter
    private PriceLevelUpdate lastB;

    public Tick(Instant bucket, Tick previousTick) {
        this.bucket = bucket;
        this.startingA = Optional.ofNullable(previousTick).map(p -> p.lastA).orElse(null);
        this.startingB = Optional.ofNullable(previousTick).map(p -> p.lastB).orElse(null);
    }

    public Tick addAsk(PriceLevelUpdate a) {
        //
        this.as.add(a);
        this.lastA = a;
        return this;
    }

    public Tick addBid(PriceLevelUpdate b) {
        this.bs.add(b);
        this.lastB = b;
        return this;
    }

    public Integer incrementTotalUpdates() {
        numberOfUpdates++;
        return numberOfUpdates;
    }

    public Integer totalUpdates() {
        return numberOfUpdates;
    }
}
