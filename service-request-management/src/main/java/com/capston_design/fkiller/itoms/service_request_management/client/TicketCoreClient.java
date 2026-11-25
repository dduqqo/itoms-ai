package com.capston_design.fkiller.itoms.service_request_management.client;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;

import com.capston_design.fkiller.itoms.contracts.ticket.CompleteTaskRequestDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.CompleteTicketRequestDTO;
import com.capston_design.fkiller.itoms.contracts.ticket.TicketListRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.client.dto.response.TicketInformationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ticket-core", url = "${TICKET_CORE_URL}")
public interface TicketCoreClient {
    @PostMapping("/v1/tickets/by-acceptor")
    ApiResponse<List<TicketInformationDTO>> getTicketList(@RequestBody TicketListRequestDTO request);

    @PostMapping("/callback/v1/task/{taskId}/complete")
    void updateTaskStatus(@PathVariable UUID taskId,
                          @RequestBody CompleteTaskRequestDTO request);

    @PostMapping("/callback/v1/ticket/complete")
    void updateTicketStatus(@RequestBody  CompleteTicketRequestDTO updateTicketStatusRequestDTO);
}
