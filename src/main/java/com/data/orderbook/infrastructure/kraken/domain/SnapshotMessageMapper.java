package com.data.orderbook.infrastructure.kraken.domain;

import com.data.orderbook.domain.OrderBookSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Component
public class SnapshotMessageMapper extends MessageMapper {

    public SnapshotMessageMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public OrderBookSnapshot map(TextMessage textMessage) {
        final var deserialisedMessage =
                this.<List<Object>>deserialise(textMessage).get();
        final var contentMap = (Map<String, List<List<String>>>) deserialisedMessage.get(1);
        final var pair = (String) deserialisedMessage.get(3);
        final var name = (String) deserialisedMessage.get(2);
        final var asks = map(contentMap.get("as"));
        final var bids = map(contentMap.get("bs"));
        return new OrderBookSnapshot(pair, null, name, asks, bids);
    }
}
