package com.crispinlab.prestudyframework.application.user

import com.crispinlab.Snowflake
import com.crispinlab.prestudyframework.application.user.port.UserCommandPort
import com.crispinlab.prestudyframework.application.user.port.UserQueryPort
import com.crispinlab.prestudyframework.common.util.Log
import com.crispinlab.prestudyframework.domain.user.User
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
internal class UserService(
    private val snowflake: Snowflake,
    private val passwordHelper: PasswordHelper,
    private val userCommandPort: UserCommandPort,
    private val userQueryPort: UserQueryPort
) : UserCommandUseCase {
    private val logger: Logger = Log.getLogger(UserService::class.java)

    override fun registerUser(
        request: UserCommandUseCase.RegisterRequest
    ): UserCommandUseCase.RegisterResponse =
        Log.logging(logger) { log ->
            if (userQueryPort.existBy(request.username)) {
                log["registerFail"] = "duplicate username: ${request.username}"
                return@logging UserCommandUseCase.RegisterResponse.fail("duplicate username")
            }

            val user =
                User(
                    id = snowflake.nextId(),
                    username = request.username,
                    password = passwordHelper.encode(request.password)
                )

            userCommandPort.register(user)
            return@logging UserCommandUseCase.RegisterResponse.success()
        }
}
