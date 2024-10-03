package com.data.orderbook.infrastructure.kraken.domain.mapper;

import com.data.orderbook.domain.model.OrderBookUpdate;
import com.data.orderbook.domain.model.PriceLevelUpdate;
import com.data.orderbook.infrastructure.kraken.domain.MessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Component
public class UpdateMessageMapper extends MessageMapper {

    public UpdateMessageMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public OrderBookUpdate map(TextMessage textMessage) {
        final var deserialisedMessage =
                this.<List<Object>>deserialise(textMessage).get();
        if (deserialisedMessage.size() == 5) {
            // Bids and asks
            final var contentMapA = (Map<String, List<List<String>>>) deserialisedMessage.get(1);
            final var contentMapBC = (Map<String, Object>) deserialisedMessage.get(2);
            final var pair = (String) deserialisedMessage.get(4);
            final var name = (String) deserialisedMessage.get(3);
            final var asks = map(contentMapA.get("a"));
            final var bids = map((List<List<String>>) contentMapBC.get("b"));
            final var checksum = (String) contentMapBC.get("c");
            // FIXME Need to map condition on last update
            return new OrderBookUpdate(pair, null, name, asks, bids, checksum);
        }
        final var contentMap = (Map<String, Object>) deserialisedMessage.get(1);
        final var pair = (String) deserialisedMessage.get(3);
        final var name = (String) deserialisedMessage.get(2);
        final List<PriceLevelUpdate> asks;
        final List<PriceLevelUpdate> bids;
        final String checksum = (String) contentMap.get("c");
        if (contentMap.containsKey("a")) {
            asks = map((List<List<String>>) contentMap.get("a"));
            bids = null;
        } else if (contentMap.containsKey("b")) {
            bids = map((List<List<String>>) contentMap.get("b"));
            asks = null;
        } else {
            log.error("Update without Bids or Asks");
            throw new IllegalArgumentException("Update without Bids or Asks");
        }
        // FIXME Need to map condition on last update
        return new OrderBookUpdate(pair, null, name, asks, bids, checksum);
    }
}
