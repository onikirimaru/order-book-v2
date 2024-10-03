package com.data.orderbook.infrastructure.kraken.domain;

import com.data.orderbook.domain.model.PriceLevelUpdate;
import com.data.orderbook.domain.model.UpdateType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@Component
public class MessageMapper {

    public static final int MICRO_TO_NANO = 1000;
    private final ObjectMapper objectMapper;

    public <T> Try<T> deserialise(TextMessage message) {
        return Try.of(() -> objectMapper.readValue(
                message.getPayload().getBytes(StandardCharsets.UTF_8), new TypeReference<>() {}));
    }

    public <T> Try<byte[]> serialize(T object) {
        return Try.of(() -> objectMapper.writeValueAsBytes(object));
    }

    public List<PriceLevelUpdate> map(List<List<String>> prices) {
        // FIXME Handl republish events
        return prices.stream()
                .map(ple -> {
                    var epochSplit = ple.get(2).split("\\.");
                    UpdateType updateType = null;
                    if (ple.size() == 4) {
                        updateType = UpdateType.REPUBLISH;
                    }
                    return new PriceLevelUpdate(
                            new BigDecimal(ple.getFirst()),
                            new BigDecimal(ple.get(1)),
                            Instant.ofEpochSecond(
                                    Long.parseLong(epochSplit[0]), Long.parseLong(epochSplit[1]) * MICRO_TO_NANO),
                            updateType);
                })
                .toList();
    }

    public List<PriceLevelUpdate> mapNullable(List<List<String>> prices) {
        return Optional.ofNullable(prices).map(this::map).orElse(List.of());
    }
}
