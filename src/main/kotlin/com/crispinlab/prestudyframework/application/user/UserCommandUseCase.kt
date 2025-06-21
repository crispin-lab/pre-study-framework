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

            fun fail(message: String): RegisterResponse {
                return RegisterResponse(code = 300, message = message)
            }
        }
    }

    data class LoginRequest(
        val username: String,
        val password: String
    )

    class LoginResponse private constructor(
        val code: Int,
        val message: String,
        val token: String? = null
    ) {
        companion object {
            fun success(token: String): LoginResponse {
                return LoginResponse(code = 200, message = "success", token = token)
            }

            fun fail(message: String): LoginResponse {
                return LoginResponse(code = 300, message = message)
            }
        }
    }

    fun registerUser(request: RegisterRequest): RegisterResponse

    fun loginUser(request: LoginRequest): LoginResponse
}
