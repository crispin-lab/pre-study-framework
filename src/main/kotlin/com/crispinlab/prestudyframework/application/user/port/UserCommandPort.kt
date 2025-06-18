package com.crispinlab.prestudyframework.application.user.port

import com.crispinlab.prestudyframework.domain.user.User

interface UserCommandPort {
    fun register(user: User)
}
