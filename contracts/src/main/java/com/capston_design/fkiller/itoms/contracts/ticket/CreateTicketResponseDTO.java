package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 티켓 생성 결과. ticket-core -> service-desk (REST 응답, Kafka itoms.ticket.response) */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketResponseDTO {

    private UUID ticketId;
    private UUID incidentId;
}
