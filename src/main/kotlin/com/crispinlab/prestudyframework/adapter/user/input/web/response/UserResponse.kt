package com.crispinlab.prestudyframework.adapter.user.input.web.response

import com.fasterxml.jackson.annotation.JsonInclude

internal class UserResponse<T> private constructor(
    private val code: Int,
    private val message: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private val result: T? = null
) {
    companion object {
        fun success(
            code: Int,
            message: String
        ): UserResponse<Unit> = UserResponse(code = code, message = message)

        fun <T> fail(
            code: Int,
            message: String,
            result: T
        ): UserResponse<T> = UserResponse(code = code, message = message, result = result)

        fun <T> error(
            code: Int,
            message: String
        ): UserResponse<T> = UserResponse(code = code, message = message)
    }

    fun getCode(): Int = code

    fun getMessage(): String = message

    fun getResult(): T? = result
}
