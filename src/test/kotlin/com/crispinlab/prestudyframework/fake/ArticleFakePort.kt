package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.domain.article.Article

internal class ArticleFakePort : ArticleQueryPort {
    private val storage: MutableMap<Long, Article> = mutableMapOf()

    fun singleArticleFixture() {
        storage[1L] =
            Article(
                id = 1L,
                title = "테스트 게시글",
                content = "테스트",
                author = 1L,
                password = "abcDEF123"
            )
    }

    override fun count(pageLimit: Int): Int = storage.values.take(pageLimit).count()

    override fun retrieveAll(
        page: Int,
        pageSize: Int
    ): List<Article> {
        val offset: Int = (page - 1) * pageSize
        return storage.values
            .asSequence()
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(pageSize)
            .toList()
    }
}
