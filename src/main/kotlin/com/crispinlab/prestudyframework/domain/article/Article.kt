package com.crispinlab.prestudyframework.domain.article

import java.time.Instant

internal data class Article(
    val id: Long = 0L,
    var title: String,
    var content: String,
    val author: Long,
    val password: String,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
) {
    fun update(
        title: String?,
        content: String?
    ): Article {
        title?.let { this.title = it }
        content?.let { this.content = it }
        this.updatedAt = Instant.now()
        return this
    }
}
