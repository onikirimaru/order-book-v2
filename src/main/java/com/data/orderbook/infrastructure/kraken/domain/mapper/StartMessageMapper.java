package com.data.orderbook.infrastructure.kraken.domain.mapper;

import com.data.orderbook.infrastructure.kraken.domain.MessageMapper;
import com.data.orderbook.infrastructure.kraken.domain.StartEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Component
public class StartMessageMapper extends MessageMapper {

    public StartMessageMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public StartEvent map(TextMessage textMessage) {
        // FIXME TBD
        return null;
    }
}
