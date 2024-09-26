package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;


@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter(onMethod = @__(@JsonProperty))
public class Subscription {
    private final String name;
}

