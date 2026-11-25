package com.capston_design.fkiller.itoms.service_request_management.controller;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import com.capston_design.fkiller.itoms.common.core.time.ClockHolder;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.request.AssignedTicketListRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.request.completeTicketRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.AssignedTicketListResponseDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.TicketInformationResponseDTO;
import com.capston_design.fkiller.itoms.service_request_management.service.TicketInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-request-management/ticket-info")
@RequiredArgsConstructor
public class TicketInformationController {

    private final TicketInformationService ticketInformationService;
    private final ClockHolder clockHolder;

    @PostMapping("/v1/tickets")
    public ResponseEntity<ApiResponse<AssignedTicketListResponseDTO>> getTicketList(@RequestBody AssignedTicketListRequestDTO request) {
        List<TicketInformationResponseDTO> ticketInformationServiceTicketList =
                ticketInformationService.getTicketList(request.getChargerId());
        return ResponseEntity.ok(ApiResponse.onSuccess(new AssignedTicketListResponseDTO(ticketInformationServiceTicketList)));
    }

    @PostMapping("/v1/ticket/complete")
    public ResponseEntity<ApiResponse<Void>> completeTicket(@RequestBody completeTicketRequestDTO request) {
        ticketInformationService.completeTicket(request, clockHolder.now());
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

}
