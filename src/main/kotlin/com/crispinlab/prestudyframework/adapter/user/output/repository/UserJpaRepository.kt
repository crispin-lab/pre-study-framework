package com.crispinlab.prestudyframework.adapter.user.output.repository

import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): UserEntity
}
