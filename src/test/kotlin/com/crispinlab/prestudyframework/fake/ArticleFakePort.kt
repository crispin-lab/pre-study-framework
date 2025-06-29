package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.article.port.ArticleCommandPort
import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.domain.article.Article

internal class ArticleFakePort : ArticleQueryPort, ArticleCommandPort {
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

    fun clear() {
        storage.clear()
    }

    override fun count(pageLimit: Int): Int = storage.values.take(pageLimit).count()

    override fun findAllBy(
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

    override fun findBy(id: Long): Article? = storage[id]

    override fun save(article: Article): Long {
        storage[article.id] = article
        return article.id
    }

    override fun delete(id: Long) {
        storage.remove(id)
    }
}
