package com.crispinlab.prestudyframework.application.user

import com.crispinlab.Snowflake
import com.crispinlab.prestudyframework.fake.UserFakeRegisterPort
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserServiceTest {
    private lateinit var userService: UserService
    private lateinit var userFakeRegisterPort: UserFakeRegisterPort
    private val snowflake = Snowflake.create(nodeId = 100)

    @BeforeEach
    fun setUp() {
        userFakeRegisterPort = UserFakeRegisterPort()
        userService =
            UserService(
                userRegisterPort = userFakeRegisterPort,
                snowflake = snowflake
            )
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
                val request =
                    RegisterUserUseCase.RegisterRequest(
                        username = "test_user",
                        password = "1234"
                    )

                // when
                val actual: RegisterUserUseCase.RegisterResponse = userService.registerUser(request)

                // then
                Assertions.assertThat(actual).isNotNull
                Assertions.assertThat(actual.code).isEqualTo(200)
                Assertions.assertThat(actual.message).isEqualTo("success")
                Assertions.assertThat(userFakeRegisterPort.findBy(request.username))
                    .isNotNull()
                    .extracting { it!!.username }
                    .isEqualTo(request.username)
            }
        }
    }
}
