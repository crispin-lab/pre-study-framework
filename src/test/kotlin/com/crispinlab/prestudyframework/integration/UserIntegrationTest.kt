package com.crispinlab.prestudyframework.integration

import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserLoginRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
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

class UserIntegrationTest : AbstractIntegrationTest() {
    @Nested
    @DisplayName("회원 가입 통합 테스트")
    inner class RegisterUserTest() {
        @Nested
        @DisplayName("회원 가입 성공 통합 테스트")
        inner class RegisterUserSuccessTest() {
            @Test
            @DisplayName("회원 가입 성공 테스트")
            fun registerUserTest() {
                // given
                val request =
                    UserRegisterRequest(
                        username = "test09",
                        password = "abcDEF123"
                    )

                // when
                val response: Response =
                    Given {
                        log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        body(request)
                    } When {
                        post("/api/users")
                    } Then {
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
    @DisplayName("로그인 통합 테스트")
    inner class LoginUserTest() {
        @Nested
        @DisplayName("로그인 성공 통합 테스트")
        inner class LoginUserSuccessTest() {
            @Test
            @DisplayName("로그인 성공 테스트")
            fun loginUserTest() {
                // given
                val request =
                    UserLoginRequest(
                        username = "test09",
                        password = "abcDEF123"
                    )
                UserSteps.singleRegisterUser()

                // when
                val response: Response =
                    Given {
                        log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                        accept("application/vnd.pre-study.com-v1+json")
                        body(request)
                    } When {
                        post("/api/users/login")
                    } Then {
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
                    softAssertions.assertThat(response.header("Authorization"))
                        .isNotNull
                }
            }
        }
    }
}
