package com.crispinlab.prestudyframework.adapter.user.input.web

import com.crispinlab.prestudyframework.adapter.article.input.filter.JWTFilter
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserLoginRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import com.crispinlab.prestudyframework.fake.AuthHeaderFakeBuilder
import com.crispinlab.prestudyframework.fake.UserFakeCommandUserCase
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@Import(UserFakeCommandUserCase::class, AuthHeaderFakeBuilder::class)
@WebMvcTest(
    controllers = [UserCommandController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JWTFilter::class]
        )
    ]
)
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

        @Nested
        @DisplayName("회원 가입 요청 실패 테스트")
        inner class UserRegisterAPIFailTest() {
            @Test
            @DisplayName("잘못된 username로 회원가입 요청 시 요청에 실패해야 한다.")
            fun registerAPITest() {
                // given
                val request =
                    UserRegisterRequest(
                        username = "wrongUsername",
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
                        MockMvcResultMatchers.jsonPath("$.code").value(101),
                        MockMvcResultMatchers.jsonPath("$.message").value("invalid request value."),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].field")
                            .value("username"),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].value")
                            .value(request.username)
                    )
            }

            @Test
            @DisplayName("잘못된 password로 회원가입 요청 시 요청에 실패해야 한다.")
            fun registerAPITest2() {
                // given
                val request =
                    UserRegisterRequest(
                        username = "test09",
                        password = "wrongPassword"
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
                        MockMvcResultMatchers.jsonPath("$.code").value(101),
                        MockMvcResultMatchers.jsonPath("$.message").value("invalid request value."),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].field")
                            .value("password"),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].value")
                            .value(request.password)
                    )
            }
        }
    }

    @Nested
    @DisplayName("로그인 요청 테스트")
    inner class UserLoginAPITest() {
        @Nested
        @DisplayName("로그인 요청 성공 테스트")
        inner class UserLoginAPISuccessTest() {
            @Test
            @DisplayName("로그인 요청을 할 수 있어야 한다.")
            fun loginAPITest() {
                // given
                val request =
                    UserLoginRequest(
                        username = "test09",
                        password = "abcDEF123"
                    )

                // when
                val result: ResultActions =
                    mockMvc
                        .perform(
                            MockMvcRequestBuilders
                                .post("/api/users/login")
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
