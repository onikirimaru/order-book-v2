package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import com.data.orderbook.domain.ports.out.CandlePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class CandleService {

    private final OrderBookServicePort orderBookService;

    private final CandlePublisher candlePublisher;
    private final ClockProvider clockProvider;


    public CandleService(OrderBookServicePort orderBookService, CandlePublisher candlePublisher, ClockProvider clockProvider) {
        this.orderBookService = orderBookService;
        this.candlePublisher = candlePublisher;
        this.clockProvider = clockProvider;
    }

    @Scheduled(cron = "0 * * * * *")
    public void dump() {
        var now = Instant.now(clockProvider.clock());
        //We need a candle per pair
        log.info("'{}' Dump candle start", now.getEpochSecond());
        orderBookService.calculateCandle(now).forEach(candlePublisher::publish);
    }
}
