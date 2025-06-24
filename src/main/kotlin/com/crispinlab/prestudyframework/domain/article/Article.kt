package com.crispinlab.prestudyframework.domain.article

import java.time.Instant

internal data class Article(
    val title: String,
    val content: String,
    val author: String,
    val password: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
