package com.crispinlab.prestudyframework.adapter.article.input.web.request

import com.crispinlab.prestudyframework.common.annotation.Password

data class DeleteArticleRequest(
    @Password
    val password: String
)
