package com.crispinlab.prestudyframework.application.user

import com.crispinlab.Snowflake
import com.crispinlab.prestudyframework.application.user.port.UserRegisterPort
import com.crispinlab.prestudyframework.common.util.Log
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
internal class UserService(
    private val snowflake: Snowflake,
    private val passwordHelper: PasswordHelper,
    private val userRegisterPort: UserRegisterPort
) : RegisterUserUseCase {
    private val logger: Logger = Log.getLogger(UserService::class.java)

    override fun registerUser(
        request: RegisterUserUseCase.RegisterRequest
    ): RegisterUserUseCase.RegisterResponse =
        Log.logging(logger) {
            return@logging RegisterUserUseCase.RegisterResponse.success()
        }
}
