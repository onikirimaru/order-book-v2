package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Objects;

public class Subscription {
    private final String name;

    public Subscription(String name) {
        this.name = name;
    }

    @JsonGetter
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
