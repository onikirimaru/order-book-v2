package com.data.orderbook.infrastructure.kraken.domain.mapper;

import com.data.orderbook.domain.model.OrderBookUpdate;
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
            final var contentMapA = (Map<String, List<List<String>>>) deserialisedMessage.get(1);
            final var contentMapBC = (Map<String, List<List<String>>>) deserialisedMessage.get(2);
            final var pair = (String) deserialisedMessage.get(4);
            final var name = (String) deserialisedMessage.get(3);
            final var asks = mapNullable(contentMapA.get("a"));
            final var bids = mapNullable(contentMapBC.get("b"));
            // FIXME Need to map condition on last update
            return new OrderBookUpdate(pair, null, name, asks, bids);
        }
        final var contentMap = (Map<String, List<List<String>>>) deserialisedMessage.get(1);
        final var pair = (String) deserialisedMessage.get(3);
        final var name = (String) deserialisedMessage.get(2);
        final var asks = mapNullable(contentMap.get("a"));
        final var bids = mapNullable(contentMap.get("b"));
        // FIXME Need to map condition on last update
        return new OrderBookUpdate(pair, null, name, asks, bids);
    }
}
