package com.capston_design.fkiller.itoms.ticket_core.controller;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import com.capston_design.fkiller.itoms.contracts.ticket.CompleteTaskRequestDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.CompleteTicketRequestDTO;
import com.capston_design.fkiller.itoms.ticket_core.controller.dto.request.AssignmentCallbackDTO;
import com.capston_design.fkiller.itoms.ticket_core.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/ticket-core/callback")
@RequiredArgsConstructor
public class TicketCallbackController
{
    private final TicketService ticketService;

    @PostMapping("/v1/assign-handler")
    public ResponseEntity<ApiResponse<Void>> handleAssigneeUpdate(@RequestBody AssignmentCallbackDTO dto) {
        ticketService.assignHandler(dto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @PostMapping("/v1/task/{taskId}/complete")
    public ResponseEntity<ApiResponse<Void>> loggingTaskStatusUpdate(@PathVariable UUID taskId, @RequestBody CompleteTaskRequestDTO request) {
        log.info("[태스크 완료 요청] - taskId={}, ticketId={}, taskName={}, taskNowStatus={}, [request time] - {}", request.getTicketId(),
                taskId, request.getTaskName(), request.getTaskStatus(), request.getCompletionTime());
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @PostMapping("/v1/ticket/complete")
    public ResponseEntity<ApiResponse<Void>> completeTicket(@RequestBody CompleteTicketRequestDTO request) {
        ticketService.updateTicketStatus(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
