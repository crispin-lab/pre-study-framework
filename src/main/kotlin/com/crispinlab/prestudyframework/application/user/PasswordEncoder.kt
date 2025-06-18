package com.crispinlab.prestudyframework.application.user

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

internal interface PasswordHelper {
    fun encode(rawPassword: String): String
}

@Component
internal class BCryptPasswordHelper(
    private val passwordEncoder: BCryptPasswordEncoder
) : PasswordHelper {
    override fun encode(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }
}
