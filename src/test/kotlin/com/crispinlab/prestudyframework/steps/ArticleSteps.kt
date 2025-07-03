package com.crispinlab.prestudyframework.steps

import com.crispinlab.prestudyframework.adapter.article.input.web.request.WriteArticleRequest
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.MediaType

object ArticleSteps {
    fun singleWriteArticle(token: String) {
        val request =
            WriteArticleRequest(
                title = "테스트 게시글",
                content = "테스트 게시글 입니다.",
                password = "abcDEF09"
            )

        val cookie: Cookie = Cookie.Builder("AUTH-TOKEN", token).build()

        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            accept("application/vnd.pre-study.com-v1+json")
            body(request)
            cookie(cookie)
        } When {
            post("/api/article")
        } Then {
            statusCode(200)
        } Extract {
            response()
        }
    }
}
