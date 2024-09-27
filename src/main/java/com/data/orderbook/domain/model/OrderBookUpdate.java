package com.data.orderbook.domain.model;

import java.util.List;

public record OrderBookUpdate(String pair, String depth, String channelName, List<PriceLevel> a, List<PriceLevel> b) {}
