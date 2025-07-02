package com.crispinlab.prestudyframework.docs

import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserLoginRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import com.crispinlab.prestudyframework.config.TestConfig
import com.crispinlab.prestudyframework.fake.AuthHeaderFakeBuilder
import com.crispinlab.prestudyframework.fake.UserFakeCommandUserCase
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
@Import(UserFakeCommandUserCase::class, AuthHeaderFakeBuilder::class, TestConfig::class)
class UserControllerDocsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp(
        context: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(
                    MockMvcRestDocumentation.documentationConfiguration(
                        restDocumentation
                    )
                )
                .build()
    }

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

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "user-register",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("User")
                            .summary("회원 가입 API")
                            .description("사용자 회원가입"),
                    snippets =
                        arrayOf(
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
                                    ).optional()
                            )
                        ),
                    requestPreprocessor =
                        Preprocessors.preprocessRequest(
                            Preprocessors.prettyPrint()
                        ),
                    responsePreprocessor =
                        Preprocessors.preprocessResponse(
                            Preprocessors.prettyPrint()
                        )
                )
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

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "user-login",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("User")
                            .summary("로그인 API")
                            .description("사용자 로그인"),
                    snippets =
                        arrayOf(
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
                                    ).optional()
                            ),
                            HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION)
                                    .description("Auth 토큰 값")
                                    .attributes(
                                        Attributes
                                            .key("auth")
                                            .value("{{Token}}")
                                    )
                            ),
                            HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName(HttpHeaders.SET_COOKIE)
                                    .description("Auth Cookie")
                                    .attributes(
                                        Attributes
                                            .key("auth-cookie")
                                            .value("{{Cookie}}")
                                    )
                            )
                        ),
                    requestPreprocessor =
                        Preprocessors.preprocessRequest(
                            Preprocessors.prettyPrint()
                        ),
                    responsePreprocessor =
                        Preprocessors.preprocessResponse(
                            Preprocessors.prettyPrint()
                        )
                )
            )
    }
}
