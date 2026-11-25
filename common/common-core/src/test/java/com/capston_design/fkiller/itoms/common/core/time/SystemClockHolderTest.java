package com.capston_design.fkiller.itoms.common.core.time;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class SystemClockHolderTest {

    private static final Instant MIDNIGHT_UTC = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void 서버_시간대가_UTC여도_KST_시각을_반환한다() {
        ClockHolder holder = new SystemClockHolder(Clock.fixed(MIDNIGHT_UTC, ZoneOffset.UTC));

        assertThat(holder.now()).isEqualTo("2026-01-01T09:00:00.000+09:00");
    }

    @Test
    void 서버_시간대가_달라도_결과가_같다() {
        Clock newYork = Clock.fixed(MIDNIGHT_UTC, java.time.ZoneId.of("America/New_York"));

        assertThat(new SystemClockHolder(newYork).now()).isEqualTo("2026-01-01T09:00:00.000+09:00");
    }

    @Test
    void now_결과를_다시_파싱할_수_있다() {
        String now = new SystemClockHolder(Clock.fixed(MIDNIGHT_UTC, ZoneOffset.UTC)).now();

        assertThat(ClockUtils.parseToLocalDateTime(now)).isEqualTo(LocalDateTime.of(2026, 1, 1, 9, 0));
    }
}
