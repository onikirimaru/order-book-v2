package com.data.orderbook.infrastructure.kraken.domain;

import com.data.orderbook.domain.OrderBookSnapshot;
import com.data.orderbook.domain.PriceLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UpdateMessageMapper {

//    public OrderBookSnapshot map(List<Object> updateEvent) {
//        if (((Map<String, List<List<String>>>) updateEvent.get(1)).containsKey("as")) {
//            return;
//        }
//        return null;
//
//    }

    public List<PriceLevel> map(List<List<String>> priceLevelEvents) {
        return null;
//        return priceLevelEvents.stream().map(ple -> new PriceLevel(ple[0], ple[1], ple[2])).toList();
    }
}
