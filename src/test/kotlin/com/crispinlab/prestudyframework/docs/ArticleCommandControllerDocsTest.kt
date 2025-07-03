package com.crispinlab.prestudyframework.docs

import com.crispinlab.prestudyframework.adapter.article.input.web.request.DeleteArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.request.UpdateArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import com.crispinlab.prestudyframework.config.TestConfig
import com.crispinlab.prestudyframework.fake.ArticleFakeCommandUseCase
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
import org.springframework.restdocs.cookies.CookieDocumentation
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
@Import(ArticleFakeCommandUseCase::class, TestConfig::class)
class ArticleCommandControllerDocsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

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
    @DisplayName("게시글을 작성을 할 수 있어야 한다.")
    fun writeArticleApiDocument() {
        // given
        val request =
            WriteArticleRequest(
                title = "테스트 게시글",
                content = "테스트 게시글 입니다.",
                password = "abcDEF09"
            )

        // when
        val result: ResultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/api/article")
                    .accept("application/vnd.pre-study.com-v1+json")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .requestAttr("userId", 1L)
                    .header("Authorization", "token")
                    .cookie(Cookie("AUTH-TOKEN", "token"))
            ).andDo(MockMvcResultHandlers.print())

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "write-article",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("Article")
                            .summary("게시글 작성 API")
                            .description("게시글 작성"),
                    snippets =
                        arrayOf(
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
                                    ).optional(),
                                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION)
                                    .description("Authentication Token")
                                    .attributes(
                                        Attributes
                                            .key(HttpHeaders.AUTHORIZATION)
                                            .value("Token")
                                    ).optional()
                            ),
                            CookieDocumentation.requestCookies(
                                CookieDocumentation.cookieWithName("AUTH-TOKEN")
                                    .description("사용자 인증 토큰")
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
    @DisplayName("게시글을 업데이트 할 수 있어야 한다.")
    fun updateArticleApiDocument() {
        // given
        val articleId = 1L
        val request =
            UpdateArticleRequest(
                title = "테스트 게시글",
                content = "테스트 게시글 입니다.",
                password = "abcDEF09"
            )

        // when
        val result: ResultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .patch("/api/articles/{id}", articleId)
                    .accept("application/vnd.pre-study.com-v1+json")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .requestAttr("userId", 1L)
                    .header("Authorization", "token")
                    .cookie(Cookie("AUTH-TOKEN", "token"))
            ).andDo(MockMvcResultHandlers.print())

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "update-article",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("Article")
                            .summary("게시글 업데이트 API")
                            .description("게시글 업데이트"),
                    snippets =
                        arrayOf(
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
                                    ).optional(),
                                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION)
                                    .description("Authentication Token")
                                    .attributes(
                                        Attributes
                                            .key(HttpHeaders.AUTHORIZATION)
                                            .value("Token")
                                    ).optional()
                            ),
                            CookieDocumentation.requestCookies(
                                CookieDocumentation.cookieWithName("AUTH-TOKEN")
                                    .description("사용자 인증 토큰")
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
    @DisplayName("게시글을 삭제 할 수 있어야 한다.")
    fun deleteArticleApiDocument() {
        // given
        val articleId = 1L
        val request =
            DeleteArticleRequest(
                password = "abcDEF09"
            )

        // when
        val result: ResultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .delete("/api/articles/{id}", articleId)
                    .accept("application/vnd.pre-study.com-v1+json")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .requestAttr("userId", 1L)
                    .header("Authorization", "token")
                    .cookie(Cookie("AUTH-TOKEN", "token"))
            ).andDo(MockMvcResultHandlers.print())

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "delete-article",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("Article")
                            .summary("게시글 삭제 API")
                            .description("게시글 삭제"),
                    snippets =
                        arrayOf(
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
                                    ).optional(),
                                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION)
                                    .description("Authentication Token")
                                    .attributes(
                                        Attributes
                                            .key(HttpHeaders.AUTHORIZATION)
                                            .value("Token")
                                    ).optional()
                            ),
                            CookieDocumentation.requestCookies(
                                CookieDocumentation.cookieWithName("AUTH-TOKEN")
                                    .description("사용자 인증 토큰")
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
