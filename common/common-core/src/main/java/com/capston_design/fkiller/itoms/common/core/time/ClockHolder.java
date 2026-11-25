package com.capston_design.fkiller.itoms.common.core.time;

/**
 * 현재 시각 제공자. 테스트에서 고정 시각으로 바꿔 끼울 수 있게 인터페이스로 둔다.
 */
public interface ClockHolder {

    /** 현재 시각을 {@code yyyy-MM-dd'T'HH:mm:ss.SSS+09:00} 형식(KST)으로 반환한다. */
    String now();
}
