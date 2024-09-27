package com.data.orderbook.domain.model;

import java.math.BigDecimal;

public record OrderBookCandle(
        Long timestamp, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long ticks) {

    // - Timestamp; timestamp at the start of the minute (usually in epoch)
    // - Open; mid price at the start of the minute
    // - High; highest mid price during the minute
    // - Low; lowest mid price during the minute
    // - Close; mid price at the end of the minute
    // - Ticks; total number of ticks observed during the minute interval
    //    Mid pricecanbecomputedby“(highest bid + lowest ask) / 2”.
}
