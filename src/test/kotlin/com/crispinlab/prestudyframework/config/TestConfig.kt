package com.crispinlab.prestudyframework.config

import com.crispinlab.prestudyframework.adapter.user.input.web.AuthHeaderBuilder
import com.crispinlab.prestudyframework.application.article.ArticleQueryUseCase
import com.crispinlab.prestudyframework.application.user.UserCommandUseCase
import com.crispinlab.prestudyframework.fake.ArticleFakeQueryUseCase
import com.crispinlab.prestudyframework.fake.AuthHeaderFakeBuilder
import com.crispinlab.prestudyframework.fake.UserFakeCommandUserCase
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

    @Bean
    @Primary
    fun userCommandUserCase(): UserCommandUseCase {
        return UserFakeCommandUserCase()
    }

    @Bean
    @Primary
    fun authHeaderBuilder(): AuthHeaderBuilder {
        return AuthHeaderFakeBuilder()
    }
}
