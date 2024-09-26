package com.data.orderbook.domain;

import java.util.List;

public record OrderBookSnapshot(
        String pair,
        String depth,
        String channelName,
        List<PriceLevel> as,
        List<PriceLevel> bs
) {
}
