package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.domain.article.Article
import org.springframework.stereotype.Repository

@Repository
internal class ArticleRepositoryImpl() : ArticleRepository {
    override fun count(pageLimit: Int): Int {
        TODO("Not yet implemented")
    }

    override fun findAllBy(
        page: Int,
        pageSize: Int
    ): List<Article> {
        TODO("Not yet implemented")
    }

    override fun findBy(id: Long): Article? {
        TODO("Not yet implemented")
    }

    override fun save(article: Article): Long {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}
