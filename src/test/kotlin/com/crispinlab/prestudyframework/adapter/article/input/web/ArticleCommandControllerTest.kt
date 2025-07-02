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
    }
}
