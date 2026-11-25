package com.capston_design.fkiller.itoms.common.core.time;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 서버 시간대와 관계없이 KST 기준 현재 시각을 반환한다.
 */
public class SystemClockHolder implements ClockHolder {

    static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private final Clock clock;

    public SystemClockHolder() {
        this(Clock.systemUTC());
    }

    SystemClockHolder(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String now() {
        return FORMATTER.format(ZonedDateTime.now(clock).withZoneSameInstant(ZONE));
    }
}
