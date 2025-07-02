package com.crispinlab.prestudyframework.adapter.article.input.web.response

import com.fasterxml.jackson.annotation.JsonInclude

internal class ArticleResponse<T> private constructor(
    private val code: Int,
    private val message: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private val result: T? = null
) {
    companion object {
        fun <T> success(
            code: Int,
            message: String,
            result: T? = null
        ): ArticleResponse<T> = ArticleResponse(code = code, message = message, result = result)

        fun <T> fail(
            code: Int,
            message: String,
            result: T? = null
        ): ArticleResponse<T> = ArticleResponse(code = code, message = message, result = result)

        fun <T> error(
            code: Int,
            message: String
        ): ArticleResponse<T> = ArticleResponse(code = code, message = message)
    }

    fun getCode(): Int = code

    fun getMessage(): String = message

    fun getResult(): T? = result
}
