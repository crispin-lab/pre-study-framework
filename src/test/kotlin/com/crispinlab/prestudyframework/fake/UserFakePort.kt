package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.domain.user.User

class UserFakePort : UserCommandPort {
    private val storage: MutableMap<Long, User> = mutableMapOf()

    fun findBy(username: String): User? = storage.values.find { it.username == username }

    override fun register(user: User) {
        TODO("Not yet implemented")
    }
}
