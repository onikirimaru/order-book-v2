package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.annotation.HandlesTypes;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter(onMethod = @__(@JsonProperty))
@Accessors(fluent = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookSubscription extends Subscription {

    private final Depth depth;

    public BookSubscription(Depth depth, String name) {
        super(name);
        this.depth = depth;
    }

}
