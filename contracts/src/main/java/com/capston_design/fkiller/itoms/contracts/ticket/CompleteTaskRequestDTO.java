package com.capston_design.fkiller.itoms.contracts.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 작업 상태 변경(완료). service-request-management -> ticket-core */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskRequestDTO {

    private UUID ticketId;
    private String taskName;
    private String taskStatus;
    private String completionTime;
}
