package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 티켓 완료 알림. ticket-core -> service-desk (REST), ticket-core -> service-request-management (Kafka) */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCompletedRequestDTO {

    private UUID ticketId;
    private UUID incidentId;
}
