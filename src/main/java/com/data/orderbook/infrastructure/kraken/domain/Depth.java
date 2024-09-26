package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Depth {
    _10(10),
    _25(25),
    _100(100),
    _500(500),
    _1000(1000);
    private final int depth;

    Depth(int depth) {
        this.depth = depth;
    }

    @JsonValue
    public int getDepth() {
        return this.depth;
    }
}
