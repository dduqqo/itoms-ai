package com.capston_design.fkiller.itoms.service_desk.client;

import com.capston_design.fkiller.itoms.contracts.user.UserResponseDTO;
import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/user/randomRequester")
    ApiResponse<UserResponseDTO> getRandomRequesterUser();
}
