package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import org.springframework.stereotype.Repository

@Repository
internal class ArticleRepositoryImpl(
    private val articleJpaRepository: ArticleJpaRepository
) : ArticleRepository {
    override fun count(pageLimit: Int): Int = articleJpaRepository.count(pageLimit)

    override fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<ArticleEntity> {
        TODO("Not yet implemented")
    }

    override fun findBy(id: Long): ArticleEntity? {
        TODO("Not yet implemented")
    }

    override fun save(article: ArticleEntity): Long {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}
