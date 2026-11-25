package com.capston_design.fkiller.itoms.common.response;

import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import com.capston_design.fkiller.itoms.common.response.exception.GeneralException;
import com.capston_design.fkiller.itoms.common.response.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** README의 응답 명세를 그대로 검증한다. */
class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void 성공_응답은_result를_담고_값이_없으면_생략한다() throws Exception {
        mockMvc.perform(get("/test/ok"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.result.name").value("itoms"));

        mockMvc.perform(get("/test/ok-empty"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    void 업무_예외는_서비스_에러코드와_HTTP_상태로_응답한다() throws Exception {
        mockMvc.perform(get("/test/business"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("TEST4041"))
                .andExpect(jsonPath("$.message").value("테스트 리소스가 없습니다."))
                .andExpect(jsonPath("$.result").doesNotExist());

        mockMvc.perform(get("/test/business-detail"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.id").value(12));
    }

    @Test
    void 입력값_검증_실패는_필드별_메시지를_result에_담는다() throws Exception {
        mockMvc.perform(post("/test/valid").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.result.title").value("제목은 필수입니다."));
    }

    @Test
    void 처리하지_못한_예외는_500이고_내부_메시지를_노출하지_않는다() throws Exception {
        mockMvc.perform(get("/test/unknown"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("COMMON500"))
                .andExpect(jsonPath("$.result").doesNotExist())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("SECRET"))));
    }

    @Test
    void Spring_MVC_기본_예외도_표준_형식으로_감싼다() throws Exception {
        mockMvc.perform(post("/test/ok"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON405"));

        mockMvc.perform(post("/test/valid").contentType(MediaType.APPLICATION_JSON).content("{broken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON400"));
    }

    @Test
    void 다른_서비스의_응답을_역직렬화할_수_있다() throws Exception {
        String json = "{\"isSuccess\":true,\"code\":\"COMMON200\",\"message\":\"성공입니다.\",\"result\":{\"name\":\"itoms\"}}";

        ApiResponse<Map<String, String>> response = new ObjectMapper().readValue(json, new TypeReference<>() {});

        assertThat(response.getIsSuccess()).isTrue();
        assertThat(response.getResult()).containsEntry("name", "itoms");
    }

    @Test
    void 자동설정이_예외_핸들러를_등록한다() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(CommonResponseAutoConfiguration.class))
                .run(context -> assertThat(context).hasSingleBean(GlobalExceptionHandler.class));
    }

    @Getter
    @RequiredArgsConstructor
    enum TestErrorCode implements BaseErrorCode {
        NOT_FOUND(HttpStatus.NOT_FOUND, "TEST4041", "테스트 리소스가 없습니다.");

        private final HttpStatus httpStatus;
        private final String code;
        private final String message;
    }

    record ValidRequest(@NotBlank(message = "제목은 필수입니다.") String title) {
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/ok")
        ApiResponse<Map<String, String>> ok() {
            return ApiResponse.onSuccess(Map.of("name", "itoms"));
        }

        @GetMapping("/ok-empty")
        ApiResponse<Void> okEmpty() {
            return ApiResponse.onSuccess(null);
        }

        @GetMapping("/business")
        void business() {
            throw new GeneralException(TestErrorCode.NOT_FOUND);
        }

        @GetMapping("/business-detail")
        void businessDetail() {
            throw new GeneralException(TestErrorCode.NOT_FOUND, Map.of("id", 12));
        }

        @PostMapping("/valid")
        void valid(@RequestBody @Valid ValidRequest request) {
        }

        @GetMapping("/unknown")
        void unknown() {
            throw new IllegalStateException("SECRET: select * from users");
        }
    }
}
