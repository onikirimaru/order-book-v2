package com.data.orderbook.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderBook {

    private final String pair;
    private final Map<String, List<Ticks>> ticks = new HashMap<>();
}
