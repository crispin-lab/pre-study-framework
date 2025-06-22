package com.crispinlab.prestudyframework.adapter.user.input.web

import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import com.crispinlab.prestudyframework.fake.UserFakeCommandUserCase
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@Import(UserFakeCommandUserCase::class)
@WebMvcTest(UserCommandController::class)
class UserCommandControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @Nested
    @DisplayName("회원 가입 요청 테스트")
    inner class UserRegisterAPITest() {
        @Nested
        @DisplayName("회원 가입 요청 성공 테스트")
        inner class UserRegisterAPISuccessTest() {
            @Test
            @DisplayName("회원 가입 요청을 할 수 있어야 한다.")
            fun registerAPITest() {
                // given
                val request =
                    UserRegisterRequest(
                        username = "test09",
                        password = "abcDEF123"
                    )

                // when
                val result: ResultActions =
                    mockMvc
                        .perform(
                            MockMvcRequestBuilders
                                .post("/api/users")
                                .accept("application/vnd.pre-study.com-v1+json")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                        ).andDo(MockMvcResultHandlers.print())

                // then
                result
                    .andExpectAll(
                        MockMvcResultMatchers.status().isOk,
                        MockMvcResultMatchers.jsonPath("$.code").value(200),
                        MockMvcResultMatchers.jsonPath("$.message").value("success")
                    )
            }
        }
    }
}
