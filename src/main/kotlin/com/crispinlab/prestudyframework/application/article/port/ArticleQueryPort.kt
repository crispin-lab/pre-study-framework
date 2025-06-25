package com.crispinlab.prestudyframework.application.article.port

import com.crispinlab.prestudyframework.domain.article.Article

internal interface ArticleQueryPort {
    fun count(pageLimit: Int): Int

    fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<Article>

    fun findBy(id: Long): Article?
}
