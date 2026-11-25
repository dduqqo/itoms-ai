package com.capston_design.fkiller.itoms.service_request_management.service;

import com.capston_design.fkiller.itoms.contracts.ticket.TicketListRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.client.TicketCoreClient;
import com.capston_design.fkiller.itoms.common.response.exception.GeneralException;
import com.capston_design.fkiller.itoms.service_request_management.common.exception.SrErrorCode;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.request.completeTicketRequestDTO;
import com.capston_design.fkiller.itoms.service_request_management.controller.dto.response.TicketInformationResponseDTO;
import com.capston_design.fkiller.itoms.service_request_management.domain.entity.ticket_information.TicketInformation;
import com.capston_design.fkiller.itoms.service_request_management.domain.entity.ticket_information.TicketStatus;
import com.capston_design.fkiller.itoms.service_request_management.domain.ticket.TicketDomain;
import com.capston_design.fkiller.itoms.service_request_management.domain.ticket.TicketMapper;
import com.capston_design.fkiller.itoms.service_request_management.repository.TicketInformationRepository;
import com.capston_design.fkiller.itoms.service_request_management.service.dto.TicketStatusUpdateEvent;
import com.capston_design.fkiller.itoms.service_request_management.service.event.rest.RestTicketEventListener;
import com.capston_design.fkiller.itoms.service_request_management.service.event.KafkaTicketStatusPublisher;
import com.capston_design.fkiller.itoms.service_request_management.validator.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketInformationServiceImpl implements TicketInformationService {

    private final TicketCoreClient ticketCoreClient;
    private final TicketInformationRepository ticketInformationRepository;
    private final RestTicketEventListener restTicketEventListener;
    private final KafkaTicketStatusPublisher kafkaTicketStatusPublisher;
    private final TicketMapper ticketMapper;
    private final TaskValidator taskValidator;

    @Override
    @Transactional
    public List<TicketInformationResponseDTO> getTicketList(String acceptorId) {
        return Optional.ofNullable(ticketCoreClient.getTicketList(new TicketListRequestDTO(acceptorId)))
                .map(ticketMapper::toTicketDomainList)
                .map(ticketDomainList -> {
                    ticketInformationRepository.saveAll(ticketMapper.toTicketInformationEntityList(ticketDomainList));
                    return ticketMapper.toTicketInformationResponseDTOList(ticketDomainList);
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public TicketDomain getTicketInformation(UUID ticketId) {
        TicketInformation ticketInformation = ticketInformationRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new GeneralException(SrErrorCode.INVALID_TICKET));
        return ticketMapper.toTicketDomain(ticketInformation);
    }

    @Override
    public void completeTicket(completeTicketRequestDTO request, String completionTime) {
        TicketInformation ticketInformation = ticketInformationRepository.findByTicketId(request.getTicketId())
                .orElseThrow(() -> new GeneralException(SrErrorCode.INVALID_TICKET));

        if(taskValidator.checkAllTaskCompleted(request.getTicketId())){
            ticketInformation.updateTicketStatus(TicketStatus.COMPLETE_EXECUTION);
            TicketStatusUpdateEvent event = new TicketStatusUpdateEvent(request.getTicketId(), ticketInformation.getTicketName(),
                    TicketStatus.COMPLETE_EXECUTION.getCodeName(), completionTime);
            restTicketEventListener.handleTicketStatusUpdateEvent(event);
            kafkaTicketStatusPublisher.handleTicketStatusUpdateEvent(event);
            ticketInformationRepository.save(ticketInformation);
        } else {
            throw new GeneralException(SrErrorCode.TASKS_NOT_COMPLETED);
        }
    }

}
