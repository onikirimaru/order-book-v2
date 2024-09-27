package com.data.orderbook.infrastructure.kraken.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.data.orderbook.domain.PriceLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
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
}
