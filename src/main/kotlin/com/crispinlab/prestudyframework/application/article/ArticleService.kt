package com.crispinlab.prestudyframework.application.article

import com.crispinlab.prestudyframework.application.article.port.ArticleQueryPort
import org.springframework.stereotype.Service

@Service
internal class ArticleService(
    private val articleQueryPort: ArticleQueryPort
) : ArticleQueryUseCase
