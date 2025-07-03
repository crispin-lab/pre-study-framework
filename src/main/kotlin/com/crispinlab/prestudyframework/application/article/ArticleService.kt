package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.application.article.port.ArticleCommandPort
import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import com.crispinlab.prestudyframework.application.user.PasswordHelper
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.common.util.Log
import com.crispinlab.prestudyframework.common.util.PageLimitCalculator
import com.crispinlab.prestudyframework.common.util.SnowflakeIdCreator
import com.crispinlab.prestudyframework.domain.article.Article
import com.crispinlab.prestudyframework.domain.user.User
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class ArticleService(
    private val articleQueryPort: ArticleQueryPort,
    private val articleCommandPort: ArticleCommandPort,
    private val userQueryPort: UserQueryPort,
    private val passwordHelper: PasswordHelper
) : ArticleQueryUseCase, ArticleCommandUseCase {
    private val logger: Logger = Log.getLogger(ArticleService::class.java)

    override fun retrieveArticles(
        params: ArticleQueryUseCase.RetrieveArticlesParams
    ): ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticlesResponse> =
        Log.logging(logger) { log ->
            log["method"] = "retrieveArticles()"
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

            return@logging ArticleQueryUseCase.Response.success(
                ArticleQueryUseCase.RetrieveArticlesResponse(
                    articles = articles.map { it.toDto(usernames) },
                    count = count
                )
            )
        }

    override fun retrieveArticle(
        id: Long
    ): ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticleResponse> =
        Log.logging(logger) { log ->
            log["method"] = "retrieveArticle()"

            val foundArticle: Article =
                articleQueryPort.findBy(id) ?: run {
                    log["retrieveFail"] = "author id: $id"
                    return@logging ArticleQueryUseCase.Response.fail("invalid article")
                }

            val foundAuthor: User =
                userQueryPort.findBy(foundArticle.author) ?: run {
                    log["writeFail"] = "author id: ${foundArticle.author}"
                    return@logging ArticleQueryUseCase.Response.fail("invalid author")
                }

            return@logging ArticleQueryUseCase.Response.success(
                ArticleQueryUseCase.RetrieveArticleResponse(
                    id = foundArticle.id,
                    title = foundArticle.title,
                    content = foundArticle.content,
                    author = foundAuthor.username,
                    createdAt = foundArticle.createdAt,
                    updatedAt = foundArticle.updatedAt
                )
            )
        }

    @Transactional
    override fun writeArticle(request: ArticleCommandUseCase.WriteArticleRequest) =
        Log.logging(logger) { log ->
            log["method"] = "writeArticle()"
            val article =
                Article(
                    id = SnowflakeIdCreator.nextId(),
                    title = request.title,
                    content = request.content,
                    author = request.author,
                    password = passwordHelper.encode(request.password)
                )

            userQueryPort.findBy(request.author) ?: run {
                log["writeFail"] = "author id: ${request.author}"
                return@logging ArticleCommandUseCase.Response.fail("invalid author")
            }

            articleCommandPort.save(article)

            return@logging ArticleCommandUseCase.Response.success()
        }

    @Transactional
    override fun updateArticle(request: ArticleCommandUseCase.UpdateArticleRequest) =
        Log.logging(logger) { log ->
            log["method"] = "updateArticle()"

            val foundArticle: Article =
                articleQueryPort.findBy(request.id) ?: run {
                    log["updateFail"] = "article id: ${request.id}"
                    return@logging ArticleCommandUseCase.Response.fail("invalid article")
                }

            if (!isAuthenticated(article = foundArticle, password = request.password, log)) {
                return@logging ArticleCommandUseCase.Response.fail("authenticate fail")
            }

            val updatedArticle: Article =
                foundArticle.update(
                    title = request.title,
                    content = request.content
                )

            articleCommandPort.update(updatedArticle)

            return@logging ArticleCommandUseCase.Response.success()
        }

    @Transactional
    override fun deleteArticle(request: ArticleCommandUseCase.DeleteArticleRequest) =
        Log.logging(logger) { log ->
            log["method"] = "deleteArticle()"

            val foundArticle: Article =
                articleQueryPort.findBy(request.id) ?: run {
                    log["updateFail"] = "article id: ${request.id}"
                    return@logging ArticleCommandUseCase.Response.fail("invalid article")
                }

            if (!isAuthenticated(article = foundArticle, password = request.password, log)) {
                return@logging ArticleCommandUseCase.Response.fail("authenticate fail")
            }

            articleCommandPort.delete(request.id)
            return@logging ArticleCommandUseCase.Response.success()
        }

    private fun isAuthenticated(
        article: Article,
        password: String,
        log: MutableMap<String, Any>
    ): Boolean {
        if (!passwordHelper.matches(rawPassword = password, encodedPassword = article.password)) {
            log["authFail"] = "articleId: ${article.id}"
            return false
        }
        return true
    }
}
