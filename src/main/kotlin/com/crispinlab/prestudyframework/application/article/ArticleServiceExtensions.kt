package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.domain.article.Article

internal fun Article.toDto(
    usernames: Map<Long, String>
): ArticleQueryUseCase.RetrieveArticleResponse =
    ArticleQueryUseCase.RetrieveArticleResponse(
        id = this.id,
        title = this.title,
        content = this.content,
        author = usernames.getValue(this.author),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
