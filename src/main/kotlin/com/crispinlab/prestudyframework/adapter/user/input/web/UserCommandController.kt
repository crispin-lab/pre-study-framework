package com.crispinlab.prestudyframework.adapter.user.input.web

import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserLoginRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.request.UserRegisterRequest
import com.crispinlab.prestudyframework.adapter.user.input.web.response.UserResponse
import com.crispinlab.prestudyframework.application.user.UserCommandUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
internal class UserCommandController(
    private val userCommandUseCase: UserCommandUseCase,
    private val authHeaderBuilder: AuthHeaderBuilder
) {
    @PostMapping(
        path = ["/users"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun registerUser(
        @RequestBody @Valid request: UserRegisterRequest
    ): UserResponse<Unit> {
        val response: UserCommandUseCase.RegisterResponse =
            userCommandUseCase.registerUser(
                UserCommandUseCase.RegisterRequest(
                    username = request.username,
                    password = request.password
                )
            )

        return UserResponse.success(
            code = response.code,
            message = response.message
        )
    }

    @PostMapping(
        path = ["/users/login"],
        produces = ["application/json", "application/vnd.pre-study.com-v1+json"]
    )
    fun loginUser(
        @RequestBody request: UserLoginRequest
    ): ResponseEntity<UserResponse<Unit>> {
        val response: UserCommandUseCase.LoginResponse =
            userCommandUseCase.loginUser(
                UserCommandUseCase.LoginRequest(
                    username = request.username,
                    password = request.password
                )
            )

        val headers: HttpHeaders =
            response.token?.let {
                authHeaderBuilder.buildHeader(it)
            } ?: HttpHeaders.EMPTY

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(
                UserResponse.success(
                    code = response.code,
                    message = response.message
                )
            )
    }
}
