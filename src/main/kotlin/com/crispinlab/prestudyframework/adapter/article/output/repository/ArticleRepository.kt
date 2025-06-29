package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.domain.article.Article

internal interface ArticleRepository {
    fun count(pageLimit: Int): Int

    fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<Article>

    fun findBy(id: Long): Article?

    fun save(article: Article): Long

    fun delete(id: Long)
}
