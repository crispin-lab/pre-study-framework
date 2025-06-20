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

    /*
    todo    :: jwt 토큰 발급 로직 추가 필요
     author :: heechoel shin
     date   :: 2025-06-20T16:37:14KST
     */
    override fun loginUser(
        request: UserCommandUseCase.LoginRequest
    ): UserCommandUseCase.LoginResponse =
        Log.logging(logger) { log ->
            val foundUser: User =
                userQueryPort.findBy(request.username) ?: run {
                    log["loginFail"] = "unregistered user: ${request.username}"
                    return@logging UserCommandUseCase.LoginResponse.fail("unregistered user")
                }

            if (!passwordHelper.matches(
                    rawPassword = request.password,
                    encodedPassword = foundUser.password
                )
            ) {
                log["loginFail"] = "invalid password: ${request.username}"
                return@logging UserCommandUseCase.LoginResponse.fail("invalid password")
            }

            return@logging UserCommandUseCase.LoginResponse.success()
        }
}
