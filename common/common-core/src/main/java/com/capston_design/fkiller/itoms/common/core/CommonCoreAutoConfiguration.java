package com.capston_design.fkiller.itoms.common.core;

import com.capston_design.fkiller.itoms.common.core.time.ClockHolder;
import com.capston_design.fkiller.itoms.common.core.time.SystemClockHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CommonCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClockHolder clockHolder() {
        return new SystemClockHolder();
    }
}
