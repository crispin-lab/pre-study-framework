package com.crispinlab.prestudyframework.adapter.article.input.web.request

import com.crispinlab.prestudyframework.common.annotation.NotEmptyOrBlank
import com.crispinlab.prestudyframework.common.annotation.Password
import com.crispinlab.prestudyframework.common.annotation.Title

data class WriteArticleRequest(
    @Title
    val title: String,
    @NotEmptyOrBlank
    val content: String,
    @Password
    val password: String
)
