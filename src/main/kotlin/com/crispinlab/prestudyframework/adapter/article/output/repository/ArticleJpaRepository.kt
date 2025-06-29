package com.crispinlab.prestudyframework.adapter.article.output.repository

import com.crispinlab.prestudyframework.adapter.article.output.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long> {
    @Query(
        value = """
            SELECT COUNT(*)
            FROM (
                SELECT articles.id
                FROM articles
                LIMIT :limit
            ) sub;
        """,
        nativeQuery = true
    )
    fun count(
        @Param("limit") limit: Int
    ): Int
}
