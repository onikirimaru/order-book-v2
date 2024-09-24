package com.data.orderbook.infrastructure.kraken.domain;

public record StartEvent(String connectionID, String event, String status, String version) {
}
