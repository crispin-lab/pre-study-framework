package com.crispinlab.prestudyframework.adapter.user.output

import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.domain.user.User
import org.springframework.stereotype.Component

@Component
class UserPersistenceAdapter : UserCommandPort, UserQueryPort {
    override fun register(user: User) {
        TODO("Not yet implemented")
    }

    override fun existBy(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun findBy(username: String): User {
        TODO("Not yet implemented")
    }
}
