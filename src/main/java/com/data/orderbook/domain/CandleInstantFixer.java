package com.data.orderbook.domain;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class CandleInstantFixer {

    public Instant fix(Instant instant) {
        long epochSecond = instant.getEpochSecond();
        long currentMinute = (epochSecond / 60) * 60;
        long nextMinute = currentMinute + 60;

        if (epochSecond - currentMinute <= 5) {
            return Instant.ofEpochSecond(currentMinute);
        }
        return Instant.ofEpochSecond(nextMinute);
    }
}
