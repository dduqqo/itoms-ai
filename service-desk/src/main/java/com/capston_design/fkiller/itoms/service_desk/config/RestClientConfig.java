package com.capston_design.fkiller.itoms.service_desk.config;

import com.capston_design.fkiller.itoms.common.web.client.HttpServiceClients;
import com.capston_design.fkiller.itoms.service_desk.client.TicketClient;
import com.capston_design.fkiller.itoms.service_desk.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public UserClient userClient(RestClient.Builder builder, @Value("${user.url}") String userServiceUrl) {
        return HttpServiceClients.create(builder, userServiceUrl, UserClient.class);
    }

    @Bean
    public TicketClient ticketClient(RestClient.Builder builder, @Value("${ticket.url}") String ticketUrl) {
        return HttpServiceClients.create(builder, ticketUrl, TicketClient.class);
    }
}
