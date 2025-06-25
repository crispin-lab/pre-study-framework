package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.domain.user.User

class UserFakePort : UserCommandPort, UserQueryPort {
    private val storage: MutableMap<Long, User> = mutableMapOf()

    fun singleUserFixture() {
        storage[1L] =
            User(
                id = 1L,
                username = "testUser09",
                password = "abcDEF09"
            )
    }

    fun clear() {
        storage.clear()
    }

    override fun findBy(username: String): User? = storage.values.find { it.username == username }

    override fun findAllBy(ids: Collection<Long>): List<User> {
        return storage.values.filter { it.id in ids }
    }

    override fun register(user: User): Long {
        storage[user.id] = user
        return user.id
    }

    override fun existBy(username: String): Boolean = storage.values.any { it.username == username }
}
