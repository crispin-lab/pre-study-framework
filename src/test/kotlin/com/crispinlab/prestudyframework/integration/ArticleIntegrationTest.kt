package com.crispinlab.prestudyframework.integration

import com.crispinlab.prestudyframework.steps.ArticleSteps
import com.crispinlab.prestudyframework.steps.UserSteps
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap

class ArticleIntegrationTest : AbstractIntegrationTest() {
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
        }
    }
}
