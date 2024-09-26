package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SubscriptionEvent(String event, List<String> pair, Subscription subscription, Integer reqid) {

    public static SubscriptionEvent ticker(List<String> pairs, Integer reqId) {
        return new SubscriptionEvent("subscribe", pairs, new Subscription("ticker"), reqId);
    }

    public static SubscriptionEvent book(List<String> pairs, Integer reqId) {
        return new SubscriptionEvent("subscribe", pairs, new BookSubscription(Depth._10, "book"), reqId);
    }
}
