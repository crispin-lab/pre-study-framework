package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.port.UserRegisterPort
import com.crispinlab.prestudyframework.domain.user.User

class UserFakeRegisterPort : UserRegisterPort {
    private val storage: MutableMap<Long, User> = mutableMapOf()

    fun findBy(username: String): User? = storage.values.find { it.username == username }
}
