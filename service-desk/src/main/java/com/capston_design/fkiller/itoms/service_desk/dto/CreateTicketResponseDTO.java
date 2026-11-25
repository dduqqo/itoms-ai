package com.capston_design.fkiller.itoms.service_desk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketResponseDTO {
    private UUID ticketId;
    private UUID incidentId;
}
