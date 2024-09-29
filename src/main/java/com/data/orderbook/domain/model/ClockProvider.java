package com.data.orderbook.domain.model;

import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class ClockProvider {

    public Clock clock() {
        return Clock.systemUTC();
    }
}
