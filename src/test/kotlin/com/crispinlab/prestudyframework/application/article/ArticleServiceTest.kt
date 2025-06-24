package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.fake.ArticleFakePort
import kotlin.test.Test
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

class ArticleServiceTest {
    private lateinit var articleService: ArticleService
    private lateinit var articleFakePort: ArticleFakePort

    @BeforeEach
    fun setUp() {
        articleFakePort = ArticleFakePort()
        articleService = ArticleService(articleFakePort)
    }

    @Nested
    @DisplayName("게시글 목록 조회 테스트")
    inner class RetrieveArticlesTest() {
        @Nested
        @DisplayName("게시글 목록 조회 성공 테스트")
        inner class RetrieveArticlesSuccessTest() {
            @Test
            @DisplayName("게시글 목록을 조회할 수 있어야 한다.")
            fun retrieveTest() {
                // given
                val params: ArticleQueryUseCase.RetrieveArticlesParams =
                    ArticleQueryUseCase.RetrieveArticlesParams(
                        page = 1,
                        pageSize = 10
                    )

                articleFakePort.singleArticleFixture()

                // when
                val actual: ArticleQueryUseCase.RetrieveArticlesResponse =
                    articleService.retrieveArticles(params)

                // then
                Assertions.assertThat(actual).isNotNull
                Assertions.assertThat(actual.articles.size).isZero
            }
        }
    }
}
