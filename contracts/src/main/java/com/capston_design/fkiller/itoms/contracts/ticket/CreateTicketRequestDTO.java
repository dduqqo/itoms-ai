package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 티켓 생성 요청. service-desk -> ticket-core (REST, Kafka itoms.ticket.request) */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequestDTO {

    private UUID incidentId;
    private RequesterDTO requester;
}
