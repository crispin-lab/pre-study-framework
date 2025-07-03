package com.crispinlab.prestudyframework.adapter.user.output

import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity
import com.crispinlab.prestudyframework.adapter.user.output.repository.UserRepository
import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.domain.user.User
import org.springframework.stereotype.Component

@Component
internal class UserPersistenceAdapter(
    private val userRepository: UserRepository
) : UserCommandPort, UserQueryPort {
    override fun register(user: User): Long =
        userRepository.save(
            UserEntity(
                id = user.id,
                username = user.username,
                password = user.password
            )
        )

    override fun existBy(username: String): Boolean = userRepository.existBy(username)

    override fun findBy(username: String): User? {
        return userRepository.findBy(username)?.let {
            User(
                id = it.id,
                username = it.username,
                password = it.password
            )
        }
    }

    override fun findBy(id: Long): User? {
        return userRepository.findBy(id)?.let {
            User(
                id = it.id,
                username = it.username,
                password = it.password
            )
        }
    }

    override fun findAllBy(ids: Collection<Long>): List<User> =
        userRepository.findAllBy(ids).map { it.toDomain() }
}
