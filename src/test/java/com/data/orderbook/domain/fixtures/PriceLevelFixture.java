package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.PriceLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PriceLevelFixture {

    public static PriceLevel create() {
        return PriceLevelFixture.create(1727348280L);
    }

    public static PriceLevel create(long buckeetEpochSecond) {
        return new PriceLevel(
                new BigDecimal("64347.60000"),
                new BigDecimal("0.00000000"),
                Instant.ofEpochSecond(buckeetEpochSecond - 10, 841901000L));
    }

    public static List<PriceLevel> createListOfTwo() {
        return createListOfTwo(1727348280L);
    }

    public static List<PriceLevel> createListOfTwo(long bucketEpochSecond) {
        return List.of(
                create(bucketEpochSecond),
                new PriceLevel(
                        new BigDecimal("64340.80000"),
                        new BigDecimal("0.64859742"),
                        Instant.ofEpochSecond(bucketEpochSecond - 20, 626592000)));
    }
}
