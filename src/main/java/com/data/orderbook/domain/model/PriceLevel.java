package com.data.orderbook.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceLevel(BigDecimal price, BigDecimal volume, Instant timestamp) {}
