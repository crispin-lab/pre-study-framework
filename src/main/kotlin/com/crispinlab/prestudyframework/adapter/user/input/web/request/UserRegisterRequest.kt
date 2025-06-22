package com.crispinlab.prestudyframework.adapter.user.input.web.request

import com.crispinlab.prestudyframework.common.annotation.Password
import com.crispinlab.prestudyframework.common.annotation.Username

internal data class UserRegisterRequest(
    @Username
    val username: String,
    @Password
    val password: String
)
