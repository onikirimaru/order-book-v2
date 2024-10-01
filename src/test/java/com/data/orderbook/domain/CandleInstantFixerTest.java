package com.data.orderbook.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CandleInstantFixerTest {

    CandleInstantFixer fixer = new CandleInstantFixer();

    @Test
    void shouldFixRemovingMillis() {
        long epochSecond = 1727767560L;
        var sourceInstant = Instant.ofEpochSecond(epochSecond, 111111);
        var result = fixer.fix(sourceInstant);
        Assertions.assertThat(result).isEqualTo(Instant.ofEpochSecond(epochSecond));
    }

    @ParameterizedTest
    @MethodSource("valuesAndResult")
    void shouldFixToNextMinute(Long source, Long expected) {
        var sourceInstant = Instant.ofEpochSecond(source, 111111);
        var result = fixer.fix(sourceInstant);
        Assertions.assertThat(result).isEqualTo(Instant.ofEpochSecond(expected));
    }

    public static Stream<Arguments> valuesAndResult() {
        return Stream.of(
                Arguments.of(1727767557L, 1727767560L),
                Arguments.of(1727767561L, 1727767560L),
                Arguments.of(1727767562L, 1727767560L),
                Arguments.of(1727767570L, 1727767620L));
    }
}
