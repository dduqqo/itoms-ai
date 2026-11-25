package com.capston_design.fkiller.itoms.service_request_management.validator;

import com.capston_design.fkiller.itoms.common.response.exception.GeneralException;
import com.capston_design.fkiller.itoms.service_request_management.common.exception.SrErrorCode;
import com.capston_design.fkiller.itoms.service_request_management.domain.entity.task.Task;
import com.capston_design.fkiller.itoms.service_request_management.domain.entity.task.TaskStatus;
import com.capston_design.fkiller.itoms.service_request_management.repository.TaskRepository;
import com.capston_design.fkiller.itoms.service_request_management.repository.TicketInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskValidator {
    private final TicketInformationRepository ticketInformationRepository;
    private final TaskRepository taskRepository;

    public boolean checkAllTaskCompleted(UUID ticketId){
        List<Task> taskList = taskRepository.findAllByTicketId(ticketId);
        if (taskList.isEmpty()) {
            throw new GeneralException(SrErrorCode.NO_ASSIGNED_TASKS);
        }
        return taskList.stream()
                .allMatch(task -> task.getTaskStatus() == TaskStatus.COMPLETED);
    }


}
