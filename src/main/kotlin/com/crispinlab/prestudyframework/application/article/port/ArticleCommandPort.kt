package com.crispinlab.prestudyframework.application.article.port

import com.crispinlab.prestudyframework.domain.article.Article

internal interface ArticleCommandPort {
    fun save(article: Article): Long

    fun update(article: Article): Long

    fun delete(id: Long)
}
