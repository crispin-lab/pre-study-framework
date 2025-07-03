package com.crispinlab.prestudyframework.adapter.article.input.web.request

import com.crispinlab.prestudyframework.common.annotation.NotEmptyOrBlank
import com.crispinlab.prestudyframework.common.annotation.Password
import com.crispinlab.prestudyframework.common.annotation.Title

data class UpdateArticleRequest(
    @Title
    val title: String? = null,
    @NotEmptyOrBlank
    val content: String? = null,
    @Password
    val password: String
)
