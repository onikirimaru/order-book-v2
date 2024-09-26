package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Objects;

public class BookSubscription extends Subscription {

    private final Depth depth;

    public BookSubscription(Depth depth, String name) {
        super(name);
        this.depth = depth;
    }

    @JsonGetter
    public Depth depth() {
        return depth;
    }

    @Override
    public String toString() {
        return "BookSubscription{" +
                "depth=" + depth + ", " +
                "name=" + this.name() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookSubscription that = (BookSubscription) o;
        return depth == that.depth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), depth);
    }
}
