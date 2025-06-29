package com.crispinlab.prestudyframework.application.article

import java.time.Instant

internal interface ArticleQueryUseCase {
    class Response<T> private constructor(
        val code: Int,
        val message: String,
        val data: T? = null
    ) {
        companion object {
            fun <T> success(data: T): Response<T> {
                return Response(code = 200, message = "success", data = data)
            }

            fun <T> fail(message: String): Response<T> {
                return Response(code = 400, message = message, data = null)
            }
        }
    }

    data class RetrieveArticlesParams(
        val page: Int,
        val pageSize: Int
    )

    data class RetrieveArticlesResponse(
        val articles: List<RetrieveArticleResponse>,
        val count: Int
    )

    data class RetrieveArticleResponse(
        val id: Long,
        val title: String,
        val content: String,
        val author: String,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    fun retrieveArticles(params: RetrieveArticlesParams): Response<RetrieveArticlesResponse>

    fun retrieveArticle(id: Long): Response<RetrieveArticleResponse>
}
