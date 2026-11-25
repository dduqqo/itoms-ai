package com.capston_design.fkiller.itoms.service_desk.client;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;

import com.capston_design.fkiller.itoms.contracts.ticket.CreateTicketRequestDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.CreateTicketResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TicketClient {

    @PostExchange("/api/ticket-core/v1/ticket")
    ApiResponse<CreateTicketResponseDTO> createTicket(@RequestBody CreateTicketRequestDTO request);
}
