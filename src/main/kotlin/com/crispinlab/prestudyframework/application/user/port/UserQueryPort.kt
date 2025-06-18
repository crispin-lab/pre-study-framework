package com.crispinlab.prestudyframework.application.user.port

interface UserQueryPort {
    fun existBy(username: String): Boolean
}
