package com.crispinlab.prestudyframework.domain.article

import java.time.Instant

internal data class Article(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val author: Long,
    val password: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
