package com.data.orderbook.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceLevelUpdate(BigDecimal price, BigDecimal volume, Instant timestamp, UpdateType updateType) {}
