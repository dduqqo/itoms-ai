package com.capston_design.fkiller.itoms.contracts.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** 랜덤 사용자 응답. user-service -> service-desk (Kafka) */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RandomUserResponseDTO {

    private UUID incidentId;
    private UUID userId;
    private String userName;
}
