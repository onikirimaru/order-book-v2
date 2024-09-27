package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.socket.TextMessage;

class SnapshotMessageMapperTest {

    SnapshotMessageMapper mapper = new SnapshotMessageMapper(new ObjectMapper());

    @ParameterizedTest
    @MethodSource("testValues")
    public void shouldMap(String value) {
        mapper.map(new TextMessage(value));
    }

    public static Stream<Arguments> testValues() {
        return Stream.of(
                Arguments.of(
                        "[119930880,{\"as\":[[\"65440.40000\",\"0.00200000\",\"1727421727.011863\"],[\"65460.00000\",\"0.01081692\",\"1727421730.300087\"],[\"65471.10000\",\"0.00010000\",\"1727421742.786400\"],[\"65475.00000\",\"2.31082980\",\"1727421633.077041\"],[\"65476.80000\",\"0.83244864\",\"1727421675.084803\"],[\"65477.00000\",\"0.30545076\",\"1727421642.268579\"],[\"65478.40000\",\"0.02200000\",\"1727421731.866237\"],[\"65479.20000\",\"0.02018685\",\"1727421731.293941\"],[\"65480.00000\",\"2.45560000\",\"1727421739.266895\"],[\"65480.90000\",\"0.00015300\",\"1727421616.976098\"]],\"bs\":[[\"65440.30000\",\"8.51286805\",\"1727421756.088632\"],[\"65440.10000\",\"0.14969247\",\"1727421757.939638\"],[\"65440.00000\",\"0.19698115\",\"1727421755.812941\"],[\"65439.70000\",\"6.49450435\",\"1727421757.939823\"],[\"65439.60000\",\"3.82031560\",\"1727421725.938221\"],[\"65438.90000\",\"0.45828184\",\"1727421747.193614\"],[\"65438.00000\",\"3.83347470\",\"1727421734.081520\"],[\"65437.60000\",\"0.45828184\",\"1727421731.387117\"],[\"65437.50000\",\"3.82043750\",\"1727421727.045974\"],[\"65433.80000\",\"0.76043226\",\"1727421757.929915\"]]},\"book-10\",\"XBT/USD\"]"));
    }
}
