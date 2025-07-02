package com.crispinlab.prestudyframework.adapter.article.input.web

import com.crispinlab.prestudyframework.adapter.article.input.filter.JWTFilter
import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import com.crispinlab.prestudyframework.fake.ArticleFakeCommandUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

@Import(ArticleFakeCommandUseCase::class)
@WebMvcTest(
    controllers = [ArticleCommandController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JWTFilter::class]
        )
    ]
)
class ArticleCommandControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @Nested
    @DisplayName("게시글 작성 요청 테스트")
    inner class WriteArticleApiTest() {
        @Nested
        @DisplayName("게시글 작성 요청 성공 테스트")
        inner class WriteArticleApiSuccessTest() {
            @Test
            @DisplayName("게시글 작성 요청을 할 수 있어야 한다.")
            fun writeApiTest() {
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
        @DisplayName("게시글 작성 실패 테스트")
        inner class WriteArticleApiFailTest() {
            @Test
            @DisplayName("잘못된 title 로 게시글 작성 요청 시 작성에 실패해야 한다.")
            fun writeApiTest() {
                // given
                val request =
                    WriteArticleRequest(
                        title = "",
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
                    ).andDo(MockMvcResultHandlers.print())

                // then
                result
                    .andExpectAll(
                        MockMvcResultMatchers.status().isOk,
                        MockMvcResultMatchers.jsonPath("$.code")
                            .value(101),
                        MockMvcResultMatchers.jsonPath("$.message")
                            .value("invalid request value."),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].field")
                            .value("title"),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].value")
                            .value(request.title)
                    )
            }

            @Test
            @DisplayName("잘못된 content 로 게시글 작성 요청 시 작성에 실패해야 한다.")
            fun writeApiTest2() {
                // given
                val request =
                    WriteArticleRequest(
                        title = "테스트 게시글",
                        content = "",
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
                    ).andDo(MockMvcResultHandlers.print())

                // then
                result
                    .andExpectAll(
                        MockMvcResultMatchers.status().isOk,
                        MockMvcResultMatchers.status().isOk,
                        MockMvcResultMatchers.jsonPath("$.code")
                            .value(101),
                        MockMvcResultMatchers.jsonPath("$.message")
                            .value("invalid request value."),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].field")
                            .value("content"),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].value")
                            .value(request.content)
                    )
            }

            @Test
            @DisplayName("title의 길이가 61 이상인 경우 작성에 실패해야 한다.")
            @Suppress("ktlint:standard:max-line-length")
            fun writeApiTest3() {
                // given
                val request =
                    WriteArticleRequest(
                        title = "테스트".repeat(20).plus("1"),
                        content = "테스트 게시글 입니다.",
                        password = "abcDEF09"
                    )
                println(request.title.length)

                // when
                val result: ResultActions =
                    mockMvc.perform(
                        MockMvcRequestBuilders
                            .post("/api/article")
                            .accept("application/vnd.pre-study.com-v1+json")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .requestAttr("userId", 1L)
                    ).andDo(MockMvcResultHandlers.print())

                // then
                result
                    .andExpectAll(
                        MockMvcResultMatchers.status().isOk,
                        MockMvcResultMatchers.jsonPath("$.code")
                            .value(101),
                        MockMvcResultMatchers.jsonPath("$.message")
                            .value("invalid request value."),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].field")
                            .value("title"),
                        MockMvcResultMatchers.jsonPath("$.result.errors[0].value")
                            .value(request.title)
                    )
            }
        }
    }
}
