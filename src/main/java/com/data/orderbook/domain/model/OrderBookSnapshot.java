package com.data.orderbook.domain.model;

import java.util.List;

public record OrderBookSnapshot(
        String pair, String depth, String channelName, List<PriceLevelUpdate> as, List<PriceLevelUpdate> bs) {}
