package com.crispinlab.prestudyframework.application.article

import java.time.Instant

interface ArticleQueryUseCase {
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

    fun retrieveArticles(params: RetrieveArticlesParams): RetrieveArticlesResponse
}
