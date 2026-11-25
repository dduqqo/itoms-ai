package com.capston_design.fkiller.itoms.ticket_core.client;

import com.capston_design.fkiller.itoms.contracts.user.UserResponseDTO;
import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface UserServiceClient {

    @GetExchange("/api/user/randomCreator")
    ApiResponse<UserResponseDTO> getRandomCreatorInfo();
}
