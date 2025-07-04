package com.crispinlab.prestudyframework.adapter.article.output

import com.crispinlab.prestudyframework.adapter.article.output.extensions.toDomain
import com.crispinlab.prestudyframework.adapter.article.output.extensions.toEntity
import com.crispinlab.prestudyframework.adapter.article.output.repository.ArticleRepository
import com.crispinlab.prestudyframework.application.article.port.ArticleCommandPort
import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.domain.article.Article
import org.springframework.stereotype.Component

@Component
internal class ArticlePersistenceAdaptor(
    private val articleRepository: ArticleRepository
) : ArticleQueryPort, ArticleCommandPort {
    override fun count(pageLimit: Int): Int = articleRepository.count(pageLimit)

    override fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<Article> =
        articleRepository.findAllBy(
            offset = (page - 1) * pageSize,
            limit = pageSize
        ).map { it.toDomain() }

    override fun findBy(id: Long): Article? = articleRepository.findBy(id)?.toDomain()

    override fun save(article: Article): Long = articleRepository.save(article.toEntity())

    override fun update(article: Article): Long = articleRepository.update(article.toEntity())

    override fun delete(id: Long) = articleRepository.delete(id)
}
