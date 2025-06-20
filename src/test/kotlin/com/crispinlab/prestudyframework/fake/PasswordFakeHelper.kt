package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.PasswordHelper

class PasswordFakeHelper : PasswordHelper {
    override fun encode(rawPassword: String): String = rawPassword

    override fun matches(
        rawPassword: CharSequence,
        encodedPassword: String
    ): Boolean = rawPassword.toString() == encodedPassword
}
