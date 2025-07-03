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

    @Query(
        value = """
            SELECT articles.id, articles.title, articles.content, articles.author, articles.password,
                articles.created_at, articles.updated_at, articles.created_by, articles.updated_by, 
                articles.is_deleted, articles.deleted_at
            FROM (
                SELECT articles.id
                FROM articles
                WHERE is_deleted = false
                LIMIT :limit OFFSET :offset
            ) sub
            LEFT JOIN articles ON sub.id = articles.id
        """,
        nativeQuery = true
    )
    fun findAllBy(
        @Param("offset") offset: Int,
        @Param("limit") limit: Int
    ): List<ArticleEntity>
}
