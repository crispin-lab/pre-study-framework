package com.crispinlab.prestudyframework.docs

import com.crispinlab.prestudyframework.adapter.user.input.web.UserCommandController
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserLoginRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import com.crispinlab.prestudyframework.fake.AuthHeaderFakeBuilder
import com.crispinlab.prestudyframework.fake.UserFakeCommandUserCase
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
@Import(UserFakeCommandUserCase::class, AuthHeaderFakeBuilder::class)
@WebMvcTest(UserCommandController::class)
class UserControllerDocsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @Test
    @DisplayName("회원 가입 요청을 할 수 있어야 한다.")
    fun registerAPIDocument() {
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
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentationWrapper.document(
                        identifier = "user-register",
                        requestPreprocessor =
                            Preprocessors.preprocessRequest(
                                Preprocessors.prettyPrint()
                            ),
                        responsePreprocessor =
                            Preprocessors.preprocessResponse(
                                Preprocessors.prettyPrint()
                            ),
                        PayloadDocumentation.requestFields(
                            PayloadDocumentation.fieldWithPath("username")
                                .type(JsonFieldType.STRING)
                                .description("사용자 이름"),
                            PayloadDocumentation.fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .description("사용자 비밀번호")
                        ),
                        PayloadDocumentation.responseFields(
                            PayloadDocumentation.fieldWithPath("code")
                                .type(JsonFieldType.NUMBER)
                                .description("응답 코드"),
                            PayloadDocumentation.fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("응답 메시지")
                        ),
                        HeaderDocumentation.requestHeaders(
                            HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT)
                                .description("API 버전 관리를 위한 ACCEPT")
                                .attributes(
                                    Attributes
                                        .key("v1")
                                        .value("application/vnd.pre-study.com-v1+json")
                                )
                        )
                    )
                )

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            )
    }

    @Test
    @DisplayName("로그인 요청을 할 수 있어야 한다.")
    fun loginAPIDocument() {
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
                )
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentationWrapper.document(
                        identifier = "user-login",
                        requestPreprocessor =
                            Preprocessors.preprocessRequest(
                                Preprocessors.prettyPrint()
                            ),
                        responsePreprocessor =
                            Preprocessors.preprocessResponse(
                                Preprocessors.prettyPrint()
                            ),
                        PayloadDocumentation.requestFields(
                            PayloadDocumentation.fieldWithPath("username")
                                .type(JsonFieldType.STRING)
                                .description("사용자 이름"),
                            PayloadDocumentation.fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .description("사용자 비밀번호")
                        ),
                        PayloadDocumentation.responseFields(
                            PayloadDocumentation.fieldWithPath("code")
                                .type(JsonFieldType.NUMBER)
                                .description("응답 코드"),
                            PayloadDocumentation.fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("응답 메시지")
                        ),
                        HeaderDocumentation.requestHeaders(
                            HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT)
                                .description("API 버전 관리를 위한 ACCEPT")
                                .attributes(
                                    Attributes
                                        .key("v1")
                                        .value("application/vnd.pre-study.com-v1+json")
                                )
                        )
                    )
                )

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            )
    }
}
