package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.PriceLevel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PriceLevelFixture {

    public static PriceLevel create() {
        return new PriceLevel(
                new BigDecimal("64347.60000"),
                new BigDecimal("0.00000000"),
                Instant.ofEpochSecond(1727348278L, 841901000L)
        );
    }

    public static List<PriceLevel> createListOfTwo() {
        return List.of(create(), new PriceLevel(
                new BigDecimal("64340.80000"),
                new BigDecimal("0.64859742"),
                Instant.ofEpochSecond(1727348278L, 626592000)));
    }
}
