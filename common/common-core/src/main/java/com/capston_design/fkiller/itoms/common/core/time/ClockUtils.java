package com.capston_design.fkiller.itoms.common.core.time;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * {@link ClockHolder#now()} 형식의 문자열을 다루는 유틸.
 */
public final class ClockUtils {

    private ClockUtils() {
    }

    /** 오프셋이 붙은 시각 문자열을 그 오프셋 기준의 LocalDateTime으로 바꾼다. */
    public static LocalDateTime parseToLocalDateTime(String clockHolderTime) {
        return OffsetDateTime.parse(clockHolderTime, SystemClockHolder.FORMATTER).toLocalDateTime();
    }
}
