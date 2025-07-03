package com.crispinlab.prestudyframework.integration

import com.crispinlab.prestudyframework.adapter.article.input.web.request.UpdateArticleRequest
import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import com.crispinlab.prestudyframework.adapter.article.output.repository.ArticleRepository
import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity
import com.crispinlab.prestudyframework.adapter.user.output.repository.UserRepository
import com.crispinlab.prestudyframework.steps.ArticleSteps
import com.crispinlab.prestudyframework.steps.UserSteps
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap

class ArticleIntegrationTest : AbstractIntegrationTest() {
    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Nested
    @DisplayName("게시글 목록 조회 통합 테스트")
    inner class RetrieveArticlesTest() {
        @Nested
        @DisplayName("게시글 목록 조회 성공 통합 테스트")
        inner class RetrieveArticlesSuccessTest() {
            @Test
            @DisplayName("게시글 목록 조회 성공 테스트")
            fun retrieveArticlesTest() {
                // given
                val params = LinkedMultiValueMap<String, String>()
                params.add("page", "1")
                params.add("pageSize", "30")

                UserSteps.singleRegisterUser()
                val token: String = UserSteps.loginUser()
                ArticleSteps.singleWriteArticle(token)

                // when
                val response: Response =
                    Given {
                        log().all()
                        contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        params(params)
                    } When {
                        get("/api/articles")
                    } Then {
                        log().all()
                        statusCode(200)
                    } Extract {
                        response()
                    }

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(response.jsonPath().getShort("code"))
                        .isEqualTo(200)
                    softAssertions.assertThat(response.jsonPath().getString("message"))
                        .isEqualTo("success")
                    softAssertions.assertThat(response.jsonPath().getInt("result.count"))
                        .isEqualTo(1)
                }
            }

            @Test
            @DisplayName("게시글 목록 조회 페이징 테스트")
            fun retrieveArticlesPagingTest() =
                runTest {
                    // given
                    val params = LinkedMultiValueMap<String, String>()
                    params.add("page", "1")
                    params.add("pageSize", "30")

                    UserSteps.singleRegisterUser()
                    val foundUser: UserEntity = userRepository.findBy("test09")!!
                    generateArticleBy(userId = foundUser.id, count = 302)

                    // when
                    val response: Response =
                        Given {
                            log().all()
                            contentType(MediaType.APPLICATION_JSON_VALUE)
                            accept("application/vnd.pre-study.com-v1+json")
                            params(params)
                        } When {
                            get("/api/articles")
                        } Then {
                            log().all()
                            statusCode(200)
                        } Extract {
                            response()
                        }

                    // then
                    SoftAssertions.assertSoftly { softAssertions ->
                        softAssertions.assertThat(response.jsonPath().getShort("code"))
                            .isEqualTo(200)
                        softAssertions.assertThat(response.jsonPath().getString("message"))
                            .isEqualTo("success")
                        softAssertions.assertThat(response.jsonPath().getInt("result.count"))
                            .isEqualTo(301)
                        softAssertions.assertThat(
                            response.jsonPath().getList<Any>("result.articles").size
                        ).isEqualTo(30)
                    }
                }
        }
    }

    @Nested
    @DisplayName("게시글 조회 통합 테스트")
    inner class RetrieveArticleTest() {
        @Nested
        @DisplayName("게시글 조회 성공 통합 테스트")
        inner class RetrieveArticleSuccessTest() {
            @Test
            @DisplayName("게시글 조회 조회 성공 테스트")
            fun retrieveArticleTest() {
                // given
                UserSteps.singleRegisterUser()
                val token: String = UserSteps.loginUser()
                ArticleSteps.singleWriteArticle(token)

                val articleId: Long = articleRepository.findAllBy(0, 1).first().id

                // when
                val response: Response =
                    Given {
                        log().all()
                        contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        pathParam("id", articleId)
                    } When {
                        get("/api/articles/{id}")
                    } Then {
                        log().all()
                        statusCode(200)
                    } Extract {
                        response()
                    }

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(response.jsonPath().getShort("code"))
                        .isEqualTo(200)
                    softAssertions.assertThat(response.jsonPath().getString("message"))
                        .isEqualTo("success")
                }
            }
        }
    }

    @Nested
    @DisplayName("게시글 작성 통합 테스트")
    inner class WriteArticleTest() {
        @Nested
        @DisplayName("게시글 작성 성공 통합 테스트")
        inner class WriteArticleSuccessTest() {
            @Test
            @DisplayName("게시글 작성 성공 테스트")
            fun writeArticleTest() {
                // given
                val request =
                    WriteArticleRequest(
                        title = "테스트 게시글",
                        content = "테스트 게시글 입니다.",
                        password = "abcDEF09"
                    )

                UserSteps.singleRegisterUser()
                val token: String = UserSteps.loginUser()

                // when
                val response: Response =
                    Given {
                        log().all()
                        contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        body(request)
                        cookie(Cookie.Builder("AUTH-TOKEN", token).build())
                    } When {
                        post("/api/article")
                    } Then {
                        log().all()
                        statusCode(200)
                    } Extract {
                        response()
                    }

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(response.jsonPath().getShort("code"))
                        .isEqualTo(200)
                    softAssertions.assertThat(response.jsonPath().getString("message"))
                        .isEqualTo("success")
                }
            }
        }
    }

    @Nested
    @DisplayName("게시글 업데이트 통합 테스트")
    inner class UpdateArticleTest() {
        @Nested
        @DisplayName("게시글 업데이트 성공 통합 테스트")
        inner class UpdateArticleSuccessTest() {
            @Test
            @DisplayName("게시글 업데이트 성공 테스트")
            fun updateArticleTest() {
                // given
                val request =
                    UpdateArticleRequest(
                        title = "업데이트 테스트",
                        content = "업데이트 중",
                        password = "abcDEF123"
                    )

                UserSteps.singleRegisterUser()
                val token: String = UserSteps.loginUser()
                ArticleSteps.singleWriteArticle(token)

                val articleId: Long = articleRepository.findAllBy(0, 1).first().id

                // when
                val response: Response =
                    Given {
                        log().all()
                        contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        body(request)
                        pathParam("id", articleId)
                        cookie(Cookie.Builder("AUTH-TOKEN", token).build())
                    } When {
                        patch("/api/articles/{id}")
                    } Then {
                        log().all()
                        statusCode(200)
                    } Extract {
                        response()
                    }

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(response.jsonPath().getShort("code"))
                        .isEqualTo(200)
                    softAssertions.assertThat(response.jsonPath().getString("message"))
                        .isEqualTo("success")
                }
            }
        }
    }

    private suspend fun generateArticleBy(
        userId: Long,
        count: Int
    ) = withContext(Dispatchers.IO) {
        (1..count).map { i ->
            async {
                articleRepository.save(
                    ArticleEntity(
                        title = "테스트 게시글 $i",
                        content = "테스트 게시글 $i 입니다.",
                        author = userId,
                        password = "abcDEF123"
                    )
                )
            }
        }
    }.awaitAll()
}
