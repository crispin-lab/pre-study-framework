package com.crispinlab.prestudyframework.adapter.article.output.entity

import com.crispinlab.prestudyframework.adapter.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLDelete(sql = "UPDATE articles SET deleted_at = NOW(), is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "articles")
internal class ArticleEntity(
    @Column(nullable = false, length = 200)
    val title: String,
    @Lob
    @Column(nullable = false)
    val content: String,
    @Column(nullable = false)
    val author: Long,
    @Column(nullable = false)
    val password: String,
    @Column(nullable = false)
    val isDeleted: Boolean = false
) : BaseEntity()
