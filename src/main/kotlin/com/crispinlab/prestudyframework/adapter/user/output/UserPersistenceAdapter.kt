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
                username = user.username,
                password = user.password
            )
        )

    override fun existBy(username: String): Boolean = userRepository.existBy(username)

    override fun findBy(username: String): User {
        val foundUserEntity: UserEntity = userRepository.findBy(username)
        return User(
            id = foundUserEntity.id,
            username = foundUserEntity.username,
            password = foundUserEntity.password
        )
    }
}
