package com.crispinlab.prestudyframework.adapter.article.input.web

import com.crispinlab.prestudyframework.fake.ArticleFakeQueryUseCase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

@Import(ArticleFakeQueryUseCase::class)
@WebMvcTest(ArticleQueryController::class)
class ArticleQueryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    @DisplayName("게시글 목록 조회 요청 테스트")
    inner class RetrieveArticlesApiTest() {
        @Nested
        @DisplayName("게시글 목록 조회 요청 성공 테스트")
        inner class RetrieveArticleApiSuccessTest() {
            @Test
            @DisplayName("게시글 목록 조회 요청을 할 수 있어야 한다.")
            fun retrieveApiTest() {
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
                    )
            }
        }
    }

    @Nested
    @DisplayName("게시글 조회 요청 테스트")
    inner class RetrieveArticleApiTest() {
        @Nested
        @DisplayName("게시글 조회 요청 성공 테스트")
        inner class RetrieveArticleApiSuccessTest() {
            @Test
            @DisplayName("게시글 목록 조회 요청을 할 수 있어야 한다.")
            fun retrieveApiTest() {
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
                    )
            }
        }
    }
}
