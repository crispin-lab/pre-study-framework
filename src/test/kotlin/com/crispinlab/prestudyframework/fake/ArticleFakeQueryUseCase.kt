package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.article.ArticleQueryUseCase
import java.time.Instant

internal class ArticleFakeQueryUseCase : ArticleQueryUseCase {
    override fun retrieveArticles(
        params: ArticleQueryUseCase.RetrieveArticlesParams
    ): ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticlesResponse> =
        ArticleQueryUseCase.Response.success(
            ArticleQueryUseCase.RetrieveArticlesResponse(
                listOf(
                    ArticleQueryUseCase.RetrieveArticleResponse(
                        id = 1L,
                        title = "테스트",
                        content = "테스트 입니다.",
                        author = "테스트 작성자",
                        createdAt = Instant.now(),
                        updatedAt = Instant.now()
                    )
                ),
                count = 1
            )
        )

    override fun retrieveArticle(
        id: Long
    ): ArticleQueryUseCase.Response<ArticleQueryUseCase.RetrieveArticleResponse> =
        ArticleQueryUseCase.Response.success(
            ArticleQueryUseCase.RetrieveArticleResponse(
                id = 1L,
                title = "테스트",
                content = "테스트 입니다.",
                author = "테스트 작성자",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
}
