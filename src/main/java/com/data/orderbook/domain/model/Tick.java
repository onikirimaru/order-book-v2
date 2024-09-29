package com.data.orderbook.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Tick {
    private final Instant bucket;
    private final List<PriceLevel> as = new LinkedList<>();
    private final List<PriceLevel> bs = new LinkedList<>();

    public Tick(Instant bucket) {
        this.bucket = bucket;
    }

    public Tick addAsk(PriceLevel a) {
        this.as.add(a);
        return this;
    }

    public Tick addBid(PriceLevel b) {
        this.bs.add(b);
        return this;
    }
}

