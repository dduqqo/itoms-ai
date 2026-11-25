package com.capston_design.fkiller.itoms.service_request_management.config;

import com.capston_design.fkiller.itoms.common.web.client.HttpServiceClients;
import com.capston_design.fkiller.itoms.service_request_management.client.TicketCoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public TicketCoreClient ticketCoreClient(RestClient.Builder builder, @Value("${TICKET_CORE_URL}") String baseUrl) {
        return HttpServiceClients.create(builder, baseUrl, TicketCoreClient.class);
    }
}
