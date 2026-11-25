package com.capston_design.fkiller.itoms.common.web.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.GetExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class HttpServiceClientsTest {

    interface HelloClient {
        @GetExchange("/api/hello")
        String hello();
    }

    @Test
    void baseUrl을_붙여_인터페이스_메서드를_호출한다() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://user-service:8080/api/hello"))
                .andRespond(withSuccess("hi", MediaType.TEXT_PLAIN));

        HelloClient client = HttpServiceClients.create(builder, "http://user-service:8080", HelloClient.class);

        assertThat(client.hello()).isEqualTo("hi");
        server.verify();
    }

    @Test
    void 스킴이_없는_주소에는_http를_붙인다() {
        assertThat(HttpServiceClients.withScheme("user-service:8080")).isEqualTo("http://user-service:8080");
        assertThat(HttpServiceClients.withScheme("https://user-service")).isEqualTo("https://user-service");
    }
}
