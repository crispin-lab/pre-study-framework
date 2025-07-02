package com.crispinlab.prestudyframework.config

import com.crispinlab.prestudyframework.application.article.ArticleQueryUseCase
import com.crispinlab.prestudyframework.fake.ArticleFakeQueryUseCase
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
internal class TestConfig {
    @Bean
    @Primary
    fun articleQueryUseCase(): ArticleQueryUseCase {
        return ArticleFakeQueryUseCase()
    }
}
