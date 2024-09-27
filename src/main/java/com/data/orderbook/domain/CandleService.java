package com.data.orderbook.domain;

import com.data.orderbook.domain.ports.in.OrderBookService;
import com.data.orderbook.domain.ports.out.CandlePublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CandleService {

    private final OrderBookService orderBookService;

    private final CandlePublisher candlePublisher;

    public CandleService(OrderBookService orderBookService, CandlePublisher candlePublisher) {
        this.orderBookService = orderBookService;
        this.candlePublisher = candlePublisher;
    }

    @Scheduled(cron = "0 * * * * *")
    public void dump() {
        //We need a candle per pair
        var candlesMap = orderBookService.
    }
}
