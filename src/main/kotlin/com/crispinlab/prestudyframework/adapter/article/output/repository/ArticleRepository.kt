package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity

internal interface ArticleRepository {
    fun count(pageLimit: Int): Int

    fun findAllBy(
        offset: Int,
        limit: Int
    ): List<ArticleEntity>

    fun findBy(id: Long): ArticleEntity?

    fun save(article: ArticleEntity): Long

    fun update(article: ArticleEntity): Long

    fun delete(id: Long)
}
