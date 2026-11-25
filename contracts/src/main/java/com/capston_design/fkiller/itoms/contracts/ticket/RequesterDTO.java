package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 티켓 요청자 정보. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequesterDTO {

    private String requesterId;
    private String requesterName;
}
