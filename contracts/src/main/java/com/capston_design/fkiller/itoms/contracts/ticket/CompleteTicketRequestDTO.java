package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 티켓 상태 변경(완료). service-request-management -> ticket-core. ticketStatus는 TicketStatus의 codeName */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTicketRequestDTO {

    private UUID ticketId;
    private String ticketName;
    private String ticketStatus;
    private String completionTime;
}
