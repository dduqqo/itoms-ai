package com.capston_design.fkiller.itoms.ticket_core.controller;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import com.capston_design.fkiller.itoms.contracts.ticket.CreateTicketRequestDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.CreateTicketResponseDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.TicketListRequestDTO;
import com.capston_design.fkiller.itoms.ticket_core.controller.dto.request.AssignAcceptorRequestDTO;
import com.capston_design.fkiller.itoms.ticket_core.controller.dto.request.UpdateTicketInfoRequestDTO;
import com.capston_design.fkiller.itoms.ticket_core.controller.dto.response.TicketInfoResponseDTO;
import com.capston_design.fkiller.itoms.ticket_core.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket-core")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/v1/ticket")
    public ResponseEntity<ApiResponse<CreateTicketResponseDTO>> createTicket(@RequestBody CreateTicketRequestDTO request) {
        UUID ticketId = ticketService.createTicket(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(new CreateTicketResponseDTO(ticketId, request.getIncidentId())));
    }
    @PatchMapping("/v1/ticket/{ticketId}/assign")
    public ResponseEntity<ApiResponse<Void>> assignAcceptor(
            @PathVariable UUID ticketId,
            @RequestBody AssignAcceptorRequestDTO request
    ) {
        ticketService.assignAcceptor(ticketId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
    @PatchMapping("/v1/ticket/{ticketId}")
    public ResponseEntity<ApiResponse<Void>> updateTicket(
            @PathVariable UUID ticketId,
            @RequestBody UpdateTicketInfoRequestDTO request
    ) {
        ticketService.updateTicketInfo(ticketId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
    @PostMapping("v1/tickets/by-acceptor")
    public ResponseEntity<ApiResponse<List<TicketInfoResponseDTO>>> getTicketsByAcceptorId(
            @RequestBody TicketListRequestDTO request) {
        List<TicketInfoResponseDTO> tickets = ticketService.findTicketsByAcceptorId(request.getAcceptorId());
        return ResponseEntity.ok(ApiResponse.onSuccess(tickets));
    }
}
