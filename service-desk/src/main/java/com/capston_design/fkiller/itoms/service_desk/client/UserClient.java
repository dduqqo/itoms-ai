package com.capston_design.fkiller.itoms.service_desk.client;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import com.capston_design.fkiller.itoms.service_desk.dto.UserCreateResponseDTO;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/user/randomMember")
    ApiResponse<UserCreateResponseDTO> getRandomRequesterUser();
}
