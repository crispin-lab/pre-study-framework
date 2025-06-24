package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.common.util.PageLimitCalculator
import com.crispinlab.prestudyframework.domain.article.Article
import org.springframework.stereotype.Service

@Service
internal class ArticleService(
    private val articleQueryPort: ArticleQueryPort
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
            articleQueryPort.retrieveAll(
                page = params.page,
                pageSize = params.pageSize
            )

        /*
        todo    :: user id 리스트로 username 조회 기능 추가
         author :: heechoel shin
         date   :: 2025-06-24T23:34:24KST
         */
        articles.map { it.author }.toSet()

        return ArticleQueryUseCase.RetrieveArticlesResponse(
            articles = emptyList(),
            count = count
        )
    }
}
