package com.capston_design.fkiller.itoms.common.web.client;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * HTTP Interface(@HttpExchange) 클라이언트를 만든다.
 * Spring Boot가 주입하는 RestClient.Builder를 넘기면 트레이싱·메트릭이 적용되어 서비스 간 호출이 한 트레이스로 이어진다.
 */
public final class HttpServiceClients {

    private HttpServiceClients() {
    }

    public static <T> T create(RestClient.Builder builder, String baseUrl, Class<T> clientType) {
        RestClient restClient = builder.clone().baseUrl(withScheme(baseUrl)).build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(clientType);
    }

    // Feign과 같이 스킴이 없으면 http://를 붙인다. (기존 환경변수가 "host:port" 형식일 수 있음)
    static String withScheme(String baseUrl) {
        return baseUrl.contains("://") ? baseUrl : "http://" + baseUrl;
    }
}
