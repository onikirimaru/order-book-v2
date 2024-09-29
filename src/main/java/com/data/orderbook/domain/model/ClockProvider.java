package com.data.orderbook.domain.model;

import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class ClockProvider {

    public Clock clock() {
        return Clock.systemUTC();
    }
}
