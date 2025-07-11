package com.crispinlab.prestudyframework.adapter.user.output.repository

import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity

internal interface UserRepository {
    fun save(user: UserEntity): Long

    fun findBy(username: String): UserEntity?

    fun findBy(id: Long): UserEntity?

    fun existBy(username: String): Boolean

    fun findAllBy(ids: Collection<Long>): List<UserEntity>
}
