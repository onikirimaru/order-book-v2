package com.data.orderbook.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceLevelUpdate(BigDecimal price, BigDecimal volume, Instant timestamp, UpdateType updateType) {}
