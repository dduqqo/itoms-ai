package com.capston_design.fkiller.itoms.service_request_management.controller;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import com.capston_design.fkiller.itoms.common.core.time.ClockHolder;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.request.CreateTasksRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.CreateTasksResponseDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.TaskResponseInformation;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.UpdateTaskStatusResponseDTO;
import com.capston_design.fkiller.itoms.service_request_management.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/service-request-management")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ClockHolder clockHolder;

    @PostMapping("/v1/task")
    public ResponseEntity<ApiResponse<CreateTasksResponseDTO>> createTask(@RequestBody CreateTasksRequestDTO request){
        TaskResponseInformation taskResponseInformation = taskService.createTask(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(new CreateTasksResponseDTO(taskResponseInformation)));
    }

    @PostMapping("/v1/task/{task_id}/status")
    public ResponseEntity<ApiResponse<UpdateTaskStatusResponseDTO>> updateTaskStatus(@PathVariable UUID task_id,
                                                        @RequestParam String taskStatus) {
        return ResponseEntity.ok(ApiResponse.onSuccess(taskService.updateTaskStatus(task_id, taskStatus, clockHolder.now())));
    }

}
