package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
internal class ArticleRepositoryImpl(
    private val articleJpaRepository: ArticleJpaRepository
) : ArticleRepository {
    override fun count(pageLimit: Int): Int = articleJpaRepository.count(pageLimit)

    override fun findAllBy(
        offset: Int,
        limit: Int
    ): List<ArticleEntity> = articleJpaRepository.findAllBy(offset = offset, limit = limit)

    override fun findBy(id: Long): ArticleEntity? = articleJpaRepository.findByIdOrNull(id)

    override fun save(article: ArticleEntity): Long = articleJpaRepository.save(article).id

    override fun delete(id: Long) = articleJpaRepository.deleteById(id)
}
