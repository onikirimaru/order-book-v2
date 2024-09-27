package com.data.orderbook.infrastructure.kraken.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.data.orderbook.domain.model.PriceLevel;
import com.data.orderbook.infrastructure.kraken.domain.mapper.UpdateMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.socket.TextMessage;

class UpdateMessageMapperTest {

    UpdateMessageMapper mapper = new UpdateMessageMapper(new ObjectMapper());

    @Test
    public void should() {
        var result = mapper.map(
                new TextMessage(
                        "[119930880,{\"b\":[[\"64347.60000\",\"0.00000000\",\"1727348278.841901\"],[\"64340.80000\",\"0.64859742\",\"1727348278.626592\",\"r\"]],\"c\":\"3020424060\"},\"book-10\",\"XBT/USD\"]"));

        assertThat(result).satisfies(r -> {
            assertThat(r.b())
                    .isEqualTo(List.of(
                            new PriceLevel(
                                    new BigDecimal("64347.60000"),
                                    new BigDecimal("0.00000000"),
                                    Instant.ofEpochSecond(1727348278L, 841901000L)),
                            new PriceLevel(
                                    new BigDecimal("64340.80000"),
                                    new BigDecimal("0.64859742"),
                                    Instant.ofEpochSecond(1727348278L, 626592000))));
        });
    }

    @ParameterizedTest
    @MethodSource("testValues")
    public void shouldMapAll(TextMessage message) {
        mapper.map(message);
    }

    public static Stream<Arguments> testValues() {
        return Stream.of(
                Arguments.of(
                        new TextMessage(
                                "[119930880,{\"a\":[[\"65530.00000\",\"4.35120597\",\"1727422308.989452\"]]},{\"b\":[[\"65529.90000\",\"3.00457718\",\"1727422308.989452\"]],\"c\":\"3297255708\"},\"book-10\",\"XBT/USD\"]")));
    }
}
