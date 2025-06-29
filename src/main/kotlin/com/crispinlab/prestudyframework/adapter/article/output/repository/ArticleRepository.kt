package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity

internal interface ArticleRepository {
    fun count(pageLimit: Int): Int

    fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<ArticleEntity>

    fun findBy(id: Long): ArticleEntity?

    fun save(article: ArticleEntity): Long

    fun delete(id: Long)
}
