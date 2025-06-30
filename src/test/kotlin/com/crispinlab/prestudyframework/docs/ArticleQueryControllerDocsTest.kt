package com.crispinlab.prestudyframework.docs

import com.crispinlab.prestudyframework.adapter.article.input.web.ArticleQueryController
import com.crispinlab.prestudyframework.fake.ArticleFakeQueryUseCase
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
@Import(ArticleFakeQueryUseCase::class)
@WebMvcTest(ArticleQueryController::class)
class ArticleQueryControllerDocsTest {
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
    @DisplayName("게시글 목록 조회 요청을 할 수 있어야 한다.")
    fun retrieveArticlesApiDocument() {
        // given
        val params = LinkedMultiValueMap<String, String>()
        params.add("page", "1")
        params.add("pageSize", "30")

        // when
        val result: ResultActions =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/articles")
                        .accept("application/vnd.pre-study.com-v1+json")
                        .params(params)
                ).andDo(MockMvcResultHandlers.print())

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "retrieve-articles",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("Article")
                            .summary("게시글 목록 조회 API")
                            .description("게시글 목록 조회"),
                    snippets =
                        arrayOf(
                            RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("page")
                                    .description("현재 페이지"),
                                RequestDocumentation.parameterWithName("pageSize")
                                    .description("페이지당 게시글 수")
                            ),
                            PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("code")
                                    .type(JsonFieldType.NUMBER)
                                    .description("응답 코드"),
                                PayloadDocumentation.fieldWithPath("message")
                                    .type(JsonFieldType.STRING)
                                    .description("응답 메시지"),
                                PayloadDocumentation.fieldWithPath("result")
                                    .type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                PayloadDocumentation.fieldWithPath("result.articles")
                                    .type(JsonFieldType.ARRAY).description("게시글 목록"),
                                PayloadDocumentation.fieldWithPath("result.articles[].id")
                                    .type(JsonFieldType.NUMBER).description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("result.articles[].title")
                                    .type(JsonFieldType.STRING).description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("result.articles[].content")
                                    .type(JsonFieldType.STRING).description("게시글 본문"),
                                PayloadDocumentation.fieldWithPath("result.articles[].author")
                                    .type(JsonFieldType.STRING).description("게시글 작성자"),
                                PayloadDocumentation.fieldWithPath("result.articles[].createdAt")
                                    .type(JsonFieldType.STRING).description("게시글 생성 일시"),
                                PayloadDocumentation.fieldWithPath("result.articles[].updatedAt")
                                    .type(JsonFieldType.STRING).description("게시글 수정 일시"),
                                PayloadDocumentation.fieldWithPath("result.count")
                                    .type(JsonFieldType.NUMBER).description("총 게시글 수")
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
    @DisplayName("게시글 상세 조회 요청을 할 수 있어야 한다.")
    fun retrieveArticleApiDocument() {
        // given
        val articleId = 1L

        // when
        val result: ResultActions =
            mockMvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/article/{id}", articleId)
                        .accept("application/vnd.pre-study.com-v1+json")
                ).andDo(MockMvcResultHandlers.print())

        // then
        result
            .andExpectAll(
                MockMvcResultMatchers.status().isOk,
                MockMvcResultMatchers.jsonPath("$.code").value(200),
                MockMvcResultMatchers.jsonPath("$.message").value("success")
            ).andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "retrieve-article",
                    resourceDetails =
                        ResourceSnippetParameters.builder()
                            .tag("Article")
                            .summary("게시글 조회 API")
                            .description("게시글 상세 조회"),
                    snippets =
                        arrayOf(
                            RequestDocumentation.pathParameters(
                                RequestDocumentation
                                    .parameterWithName("id")
                                    .description("게시글 ID")
                            ),
                            PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("code")
                                    .type(JsonFieldType.NUMBER)
                                    .description("응답 코드"),
                                PayloadDocumentation.fieldWithPath("message")
                                    .type(JsonFieldType.STRING)
                                    .description("응답 메시지"),
                                PayloadDocumentation.fieldWithPath("result")
                                    .type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                PayloadDocumentation.fieldWithPath("result.id")
                                    .type(JsonFieldType.NUMBER)
                                    .description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("result.title")
                                    .type(JsonFieldType.STRING)
                                    .description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("result.content")
                                    .type(JsonFieldType.STRING)
                                    .description("게시글 본문"),
                                PayloadDocumentation.fieldWithPath("result.author")
                                    .type(JsonFieldType.STRING)
                                    .description("게시글 작성자"),
                                PayloadDocumentation.fieldWithPath("result.createdAt")
                                    .type(JsonFieldType.STRING)
                                    .description("게시글 생성 일시"),
                                PayloadDocumentation.fieldWithPath("result.updatedAt")
                                    .type(JsonFieldType.STRING)
                                    .description("게시글 수정 일시")
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
}
