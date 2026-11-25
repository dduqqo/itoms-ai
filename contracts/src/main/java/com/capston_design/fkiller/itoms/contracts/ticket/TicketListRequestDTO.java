package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 담당자별 티켓 목록 조회. service-request-management -> ticket-core */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketListRequestDTO {

    private String acceptorId;
}
