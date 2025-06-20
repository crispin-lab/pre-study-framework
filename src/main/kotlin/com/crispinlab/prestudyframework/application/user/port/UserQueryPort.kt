package com.crispinlab.prestudyframework.application.user.port

import com.crispinlab.prestudyframework.domain.user.User

interface UserQueryPort {
    fun existBy(username: String): Boolean

    fun findBy(username: String): User?
}
