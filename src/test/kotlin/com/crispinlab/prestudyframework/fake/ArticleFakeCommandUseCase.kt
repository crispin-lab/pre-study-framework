package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.article.ArticleCommandUseCase

internal class ArticleFakeCommandUseCase : ArticleCommandUseCase {
    override fun writeArticle(
        request: ArticleCommandUseCase.WriteArticleRequest
    ): ArticleCommandUseCase.Response = ArticleCommandUseCase.Response.success()

    override fun updateArticle(
        request: ArticleCommandUseCase.UpdateArticleRequest
    ): ArticleCommandUseCase.Response = ArticleCommandUseCase.Response.success()

    override fun deleteArticle(
        request: ArticleCommandUseCase.DeleteArticleRequest
    ): ArticleCommandUseCase.Response = ArticleCommandUseCase.Response.success()
}
