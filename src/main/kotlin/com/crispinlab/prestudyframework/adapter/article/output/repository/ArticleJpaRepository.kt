package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long>
