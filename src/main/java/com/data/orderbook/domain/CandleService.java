package com.data.orderbook.domain;

import com.data.orderbook.domain.model.ClockProvider;
import com.data.orderbook.domain.model.OrderBookCandle;
import com.data.orderbook.domain.model.Tick;
import com.data.orderbook.domain.ports.in.OrderBookServicePort;
import com.data.orderbook.domain.ports.out.CandlePublisherPort;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CandleService {

    private final OrderBookServicePort orderBookService;

    private final CandlePublisherPort candlePublisher;
    private final ClockProvider clockProvider;

    public CandleService(
            OrderBookServicePort orderBookService, CandlePublisherPort candlePublisher, ClockProvider clockProvider) {
        this.orderBookService = orderBookService;
        this.candlePublisher = candlePublisher;
        this.clockProvider = clockProvider;
    }

    @Scheduled(cron = "0 * * * * *")
    public void dump() {
        var now = Instant.now(clockProvider.clock());
        // We need a candle per pair
        log.info("'{}' Dump candle start", now.getEpochSecond());
        orderBookService.ticks(now).entrySet().stream()
                .map(tickEntry -> calculateCandle(now.getEpochSecond(), tickEntry.getKey(), tickEntry.getValue()))
                .forEach(candlePublisher::publish);
    }

    private OrderBookCandle calculateCandle(long epochSecond, String key, Tick value) {
        return null;
    }
}
