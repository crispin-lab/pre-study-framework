package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.application.article.port.ArticleCommandPort
import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.application.user.PasswordHelper
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.common.util.Log
import com.crispinlab.prestudyframework.common.util.PageLimitCalculator
import com.crispinlab.prestudyframework.domain.article.Article
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
internal class ArticleService(
    private val articleQueryPort: ArticleQueryPort,
    private val articleCommandPort: ArticleCommandPort,
    private val userQueryPort: UserQueryPort,
    private val passwordHelper: PasswordHelper
) : ArticleQueryUseCase, ArticleCommandUseCase {
    private val logger: Logger = Log.getLogger(ArticleService::class.java)

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

    override fun writeArticle(request: ArticleCommandUseCase.WriteArticleRequest) =
        Log.logging(logger) { log ->
            val article =
                Article(
                    title = request.title,
                    content = request.content,
                    author = request.author,
                    password = passwordHelper.encode(request.password)
                )

            userQueryPort.findBy(request.author) ?: run {
                log["writeFail"] = "author id: ${request.author}"
                return@logging ArticleCommandUseCase.WriteArticleResponse.fail("invalid author")
            }

            articleCommandPort.save(article)

            return@logging ArticleCommandUseCase.WriteArticleResponse.success()
        }
}
