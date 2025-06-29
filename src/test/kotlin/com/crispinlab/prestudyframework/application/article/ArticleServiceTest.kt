package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.fake.ArticleFakePort
import com.crispinlab.prestudyframework.fake.PasswordFakeHelper
import com.crispinlab.prestudyframework.fake.UserFakePort
import kotlin.test.Test
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

class ArticleServiceTest {
    private lateinit var articleService: ArticleService
    private lateinit var articleFakePort: ArticleFakePort
    private lateinit var passwordFakeHelper: PasswordFakeHelper
    private lateinit var userFakePort: UserFakePort

    @BeforeEach
    fun setUp() {
        articleFakePort = ArticleFakePort()
        userFakePort = UserFakePort()
        passwordFakeHelper = PasswordFakeHelper()
        articleService =
            ArticleService(
                articleQueryPort = articleFakePort,
                userQueryPort = userFakePort,
                articleCommandPort = articleFakePort,
                passwordHelper = passwordFakeHelper
            )
        articleFakePort.clear()
        userFakePort.clear()
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

                userFakePort.singleUserFixture()
                articleFakePort.singleArticleFixture()

                // when
                val actual: ArticleQueryUseCase.RetrieveArticlesResponse =
                    articleService.retrieveArticles(params)

                // then
                Assertions.assertThat(actual).isNotNull
                Assertions.assertThat(actual.articles.size).isNotZero
            }
        }
    }

    @Nested
    @DisplayName("게시글 작성 테스트")
    inner class WriteArticleTest() {
        @Nested
        @DisplayName("게시글 작성 성공 테스트")
        inner class WriteArticleSuccessTest() {
            @Test
            @DisplayName("게시글을 작성 할 수 있어야 한다.")
            fun writeTest() {
                // given
                val request =
                    ArticleCommandUseCase.WriteArticleRequest(
                        title = "테스트 게시글",
                        content = "테스트",
                        author = 1L,
                        password = "abcDEF123"
                    )

                userFakePort.singleUserFixture()

                // when
                val actual: ArticleCommandUseCase.Response =
                    articleService.writeArticle(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(200)
                    softAssertions.assertThat(actual.message).isEqualTo("success")
                }
            }
        }

        @Nested
        @DisplayName("게시글 작성 실패 테스트")
        inner class WriteArticleFailTest() {
            @Test
            @DisplayName("존재 하지 않는 사용자 아이디로 게시글 작성 시 작성에 실패해야 한다.")
            fun writeTest() {
                // given
                val request =
                    ArticleCommandUseCase.WriteArticleRequest(
                        title = "테스트 게시글",
                        content = "테스트",
                        author = 1L,
                        password = "abcDEF123"
                    )

                // when
                val actual: ArticleCommandUseCase.Response =
                    articleService.writeArticle(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(400)
                    softAssertions.assertThat(actual.message).isEqualTo("invalid author")
                }
            }
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회 테스트")
    inner class RetrieveArticleTest() {
        @Nested
        @DisplayName("게시글 싱세 조회 성공 테스트")
        inner class RetrieveArticleSuccessTest() {
            @Test
            @DisplayName("게시글을 상세 조회 할 수 있어야 한다.")
            fun retrieveTest() {
                // given
                val articleId = 1L

                articleFakePort.singleArticleFixture()
                userFakePort.singleUserFixture()

                // when
                val actual:
                    ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticleResponse> =
                    articleService.retrieveArticle(articleId)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(200)
                    softAssertions.assertThat(actual.message).isEqualTo("success")
                }
            }
        }
    }
}
