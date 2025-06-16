package com.crispinlab.prestudyframework.application.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserServiceTest {
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = UserService()
    }

    @Nested
    @DisplayName("회원 가입 테스트")
    inner class UserRegisterTest() {
        @Nested
        @DisplayName("회원 가입 성공 테스트")
        inner class UserRegisterSuccessTest() {
            @Test
            @DisplayName("회원 가입을 할 수 있어야 한다.")
            fun registerTest() {
                // given

                // when
                userService.registerUser()

                // then
            }
        }
    }
}
