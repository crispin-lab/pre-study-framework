package com.crispinlab.prestudyframework.adapter.article.output

import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.domain.article.Article
import org.springframework.stereotype.Component

@Component
internal class ArticlePersistenceAdaptor : ArticleQueryPort {
    override fun count(pageLimit: Int): Int {
        TODO("Not yet implemented")
    }

    override fun retrieveAll(
        page: Int,
        pageSize: Int
    ): List<Article> {
        TODO("Not yet implemented")
    }
}
