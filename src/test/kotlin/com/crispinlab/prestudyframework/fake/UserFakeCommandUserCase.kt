package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.UserCommandUseCase

internal class UserFakeCommandUserCase : UserCommandUseCase {
    override fun registerUser(
        request: UserCommandUseCase.RegisterRequest
    ): UserCommandUseCase.RegisterResponse = UserCommandUseCase.RegisterResponse.success()

    override fun loginUser(
        request: UserCommandUseCase.LoginRequest
    ): UserCommandUseCase.LoginResponse {
        TODO("Not yet implemented")
    }
}
