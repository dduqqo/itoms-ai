package com.capston_design.fkiller.itoms.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> serviceDeskRoute(@Value("${SERVICE_DESK_URL:http://localhost:8080}") String serviceDeskUrl) {
        return GatewayRouterFunctions.route("service_desk")
                .route(RequestPredicates.path("/api/incident"), HandlerFunctions.http(serviceDeskUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> serviceDeskSwaggerRoute(@Value("${SERVICE_DESK_URL:http://localhost:8080}") String serviceDeskUrl) {
        return GatewayRouterFunctions.route("service_desk_swagger")
                .route(RequestPredicates.path("/aggregate/service-desk/v3/api-docs"), HandlerFunctions.http(serviceDeskUrl))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute(@Value("${USER_SERVICE_URL:http://localhost:8081}") String userServiceUrl) {
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/user"), HandlerFunctions.http(userServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute(@Value("${USER_SERVICE_URL:http://localhost:8081}") String userServiceUrl) {
        return GatewayRouterFunctions.route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"), HandlerFunctions.http(userServiceUrl))
                .filter(setPath("/v3/api-docs"))
                .build();
    }


}
