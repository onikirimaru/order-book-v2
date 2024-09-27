package com.data.orderbook.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceLevel(BigDecimal price, BigDecimal volume, Instant timestamp) {}
