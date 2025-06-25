package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.common.util.PageLimitCalculator
import com.crispinlab.prestudyframework.domain.article.Article
import org.springframework.stereotype.Service

@Service
internal class ArticleService(
    private val articleQueryPort: ArticleQueryPort,
    private val userQueryPort: UserQueryPort
) : ArticleQueryUseCase {
    override fun retrieveArticles(
        params: ArticleQueryUseCase.RetrieveArticlesParams
    ): ArticleQueryUseCase.RetrieveArticlesResponse {
        val pageLimit: Int =
            PageLimitCalculator.calculatePageLimit(
                page = params.page,
                pageSize = params.pageSize
            )
        val count: Int = articleQueryPort.count(pageLimit)

        val articles: List<Article> =
            articleQueryPort.findAllBy(
                page = params.page,
                pageSize = params.pageSize
            )

        val authorIds: Set<Long> = articles.map { it.author }.toSet()

        val usernames: Map<Long, String> =
            userQueryPort
                .findAllBy(authorIds)
                .associate { it.id to it.username }

        return ArticleQueryUseCase.RetrieveArticlesResponse(
            articles = articles.map { it.toDto(usernames) },
            count = count
        )
    }
}
