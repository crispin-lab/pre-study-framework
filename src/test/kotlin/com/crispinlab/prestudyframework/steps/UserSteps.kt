package com.crispinlab.prestudyframework.steps

import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.springframework.http.MediaType

object UserSteps {
    fun singleRegisterUser() {
        val request =
            UserRegisterRequest(
                username = "test09",
                password = "abcDEF123"
            )

        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            accept("application/vnd.pre-study.com-v1+json")
            body(request)
        } When {
            post("/api/users")
        } Then {
            statusCode(200)
        }
    }

    fun loginUser(): String {
        val request =
            UserRegisterRequest(
                username = "test09",
                password = "abcDEF123"
            )

        val response: Response =
            Given {
                contentType(MediaType.APPLICATION_JSON_VALUE)
                accept("application/vnd.pre-study.com-v1+json")
                body(request)
            } When {
                post("/api/users/login")
            } Then {
                statusCode(200)
            } Extract {
                response()
            }

        return response.cookie("AUTH-TOKEN")
    }
}
