package com.crispinlab.prestudyframework.adapter.article.input.web.request

data class WriteArticleRequest(
    val title: String,
    val content: String,
    val password: String
)
