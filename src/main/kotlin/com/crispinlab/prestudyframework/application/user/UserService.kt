package com.crispinlab.prestudyframework.application.user

import com.crispinlab.Snowflake
import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.common.util.Log
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
internal class UserService(
    private val snowflake: Snowflake,
    private val passwordHelper: PasswordHelper,
    private val userCommandPort: UserCommandPort
) : UserCommandUseCase {
    private val logger: Logger = Log.getLogger(UserService::class.java)

    override fun registerUser(
        request: UserCommandUseCase.RegisterRequest
    ): UserCommandUseCase.RegisterResponse =
        Log.logging(logger) {
            return@logging UserCommandUseCase.RegisterResponse.success()
        }
}
