package com.capston_design.fkiller.itoms.ticket_core.client;

import com.capston_design.fkiller.itoms.contracts.ticket.TicketCompletedRequestDTO;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.bind.annotation.RequestBody;

public interface IncidentClient {
    // TODO: Incident api 완료되면 구현

    // url path는 추후 변경예
    @PostExchange("/v1/ticket/complete")
    void completeTicket(@RequestBody TicketCompletedRequestDTO requestDTO);
}
