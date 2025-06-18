package com.crispinlab.prestudyframework.application.user

internal interface UserCommandUseCase {
    data class RegisterRequest(
        val username: String,
        val password: String
    )

    class RegisterResponse private constructor(
        val code: Int,
        val message: String
    ) {
        companion object {
            fun success(): RegisterResponse {
                return RegisterResponse(code = 200, message = "success")
            }
        }
    }

    fun registerUser(request: RegisterRequest): RegisterResponse
}
