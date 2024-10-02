package com.data.orderbook.domain.fixtures;

import com.data.orderbook.domain.model.PriceLevel;
import com.data.orderbook.domain.model.PriceLevelUpdate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PriceLevelUpdateFixture {

    public static PriceLevelUpdate create() {
        return PriceLevelUpdateFixture.create(1727348280L);
    }

    public static PriceLevelUpdate create(long bucketEpochSecond) {
        return new PriceLevelUpdate(
                new BigDecimal("64347.60000"),
                new BigDecimal("0.00000000"),
                Instant.ofEpochSecond(bucketEpochSecond - 10, 841901000L),
                null
        );
    }

    public static List<PriceLevelUpdate> createListOfTwo() {
        return createListOfTwo(1727348280L);
    }

    public static List<PriceLevelUpdate> createListOfTwo(long bucketEpochSecond) {
        return List.of(
                create(bucketEpochSecond),
                new PriceLevelUpdate(
                        new BigDecimal("64340.80000"),
                        new BigDecimal("0.64859742"),
                        Instant.ofEpochSecond(bucketEpochSecond - 20, 626592000),
                        null
                ));
    }
}
