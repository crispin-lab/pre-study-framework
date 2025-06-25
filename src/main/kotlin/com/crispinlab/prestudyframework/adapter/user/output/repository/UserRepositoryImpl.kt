package com.crispinlab.prestudyframework.adapter.user.output.repository

import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity
import org.springframework.stereotype.Repository

@Repository
internal class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun save(user: UserEntity): Long {
        val savedUserEntity: UserEntity = userJpaRepository.save<UserEntity>(user)
        return savedUserEntity.id
    }

    override fun findBy(username: String): UserEntity? = userJpaRepository.findByUsername(username)

    override fun existBy(username: String): Boolean = userJpaRepository.existsByUsername(username)

    override fun findAllBy(ids: Collection<Long>): List<UserEntity> =
        userJpaRepository.findAllById(ids)
}
