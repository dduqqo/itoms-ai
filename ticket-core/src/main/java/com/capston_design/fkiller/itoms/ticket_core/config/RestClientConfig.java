package com.capston_design.fkiller.itoms.ticket_core.config;

import com.capston_design.fkiller.itoms.common.web.client.HttpServiceClients;
import com.capston_design.fkiller.itoms.ticket_core.client.IncidentClient;
import com.capston_design.fkiller.itoms.ticket_core.client.UserServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public IncidentClient incidentClient(RestClient.Builder builder, @Value("${INCIDENT_URL}") String baseUrl) {
        return HttpServiceClients.create(builder, baseUrl, IncidentClient.class);
    }

    @Bean
    public UserServiceClient userServiceClient(RestClient.Builder builder, @Value("${USER_SERVICE_URL}") String baseUrl) {
        return HttpServiceClients.create(builder, baseUrl, UserServiceClient.class);
    }
}
