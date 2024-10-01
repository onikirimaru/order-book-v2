package com.data.orderbook.domain.model;

import java.math.BigDecimal;

public record OrderBookCandle(
        String id,
        String pair,
        Long timestamp,
        BigDecimal open,
        BigDecimal high,
        BigDecimal low,
        BigDecimal close,
        Integer ticks) {}
