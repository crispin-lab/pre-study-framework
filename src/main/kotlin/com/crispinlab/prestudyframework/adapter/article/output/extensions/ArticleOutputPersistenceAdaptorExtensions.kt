package com.crispinlab.prestudyframework.adapter.article.output.extensions

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import com.crispinlab.prestudyframework.domain.article.Article

internal fun Article.toEntity(): ArticleEntity =
    ArticleEntity(
        title = this.title,
        content = this.content,
        author = this.author,
        password = this.password
    )
