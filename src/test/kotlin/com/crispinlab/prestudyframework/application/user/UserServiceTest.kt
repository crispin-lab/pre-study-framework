package com.crispinlab.prestudyframework.application.user

import com.crispinlab.Snowflake
import com.crispinlab.prestudyframework.fake.JWTFakeHelper
import com.crispinlab.prestudyframework.fake.PasswordFakeHelper
import com.crispinlab.prestudyframework.fake.UserFakePort
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserServiceTest {
    private lateinit var userService: UserService
    private lateinit var userFakePort: UserFakePort
    private lateinit var passwordFakeHelper: PasswordFakeHelper
    private lateinit var jwtFakeHelper: JWTFakeHelper
    private val snowflake = Snowflake.create(nodeId = 100)

    @BeforeEach
    fun setUp() {
        userFakePort = UserFakePort()
        passwordFakeHelper = PasswordFakeHelper()
        jwtFakeHelper = JWTFakeHelper()
        userService =
            UserService(
                snowflake = snowflake,
                userCommandPort = userFakePort,
                passwordHelper = passwordFakeHelper,
                userQueryPort = userFakePort,
                jwtHelper = jwtFakeHelper
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
                    UserCommandUseCase.RegisterRequest(
                        username = "test_user",
                        password = "1234"
                    )

                // when
                val actual: UserCommandUseCase.RegisterResponse = userService.registerUser(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual).isNotNull
                    softAssertions.assertThat(actual.code).isEqualTo(200)
                    softAssertions.assertThat(actual.message).isEqualTo("success")
                    softAssertions.assertThat(userFakePort.findBy(request.username))
                        .isNotNull()
                        .extracting { it!!.username }
                        .isEqualTo(request.username)
                }
            }
        }

        @Nested
        @DisplayName("회원 가입 실패 테스트")
        inner class UserRegisterFailTest() {
            @Test
            @DisplayName("동일한 username 으로 회원가입 요청 시 회원가입이 실패해야 한다.")
            fun registerFailTest1() {
                // given
                val request =
                    UserCommandUseCase.RegisterRequest(
                        username = "test_user",
                        password = "1234"
                    )
                userService.registerUser(request)

                // when
                val actual: UserCommandUseCase.RegisterResponse = userService.registerUser(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(300)
                    softAssertions.assertThat(actual.message).isEqualTo("duplicate username")
                }
            }
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    inner class UserLoginTest() {
        @Nested
        @DisplayName("로그인 성공 테스트")
        inner class UserLoginSuccessTest() {
            @Test
            @DisplayName("로그인을 할 수 있어야 한다.")
            fun loginTest() {
                // given
                val registerRequest =
                    UserCommandUseCase.RegisterRequest(
                        username = "test_user",
                        password = "1234"
                    )
                userService.registerUser(registerRequest)

                val request =
                    UserCommandUseCase.LoginRequest(
                        username = "test_user",
                        password = "1234"
                    )

                // when
                val actual: UserCommandUseCase.LoginResponse = userService.loginUser(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(200)
                    softAssertions.assertThat(actual.message).isEqualTo("success")
                    softAssertions.assertThat(actual.token).isNotNull
                }
            }
        }

        @Nested
        @DisplayName("로그인 실패 테스트")
        inner class UserLoginFailTest() {
            @Test
            @DisplayName("등록되지 않은 유저로 로그인 요청 시 로그인에 실패해야 한다.")
            fun loginFailTest1() {
                // given
                val request =
                    UserCommandUseCase.LoginRequest(
                        username = "test_user",
                        password = "1234"
                    )

                // when
                val actual: UserCommandUseCase.LoginResponse = userService.loginUser(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(300)
                    softAssertions.assertThat(actual.message).isEqualTo("unregistered user")
                }
            }

            @Test
            @DisplayName("패스워드가 일치 하지 않는 경우 예외가 발생해야 한다.")
            fun loginFailTest2() {
                // given
                val registerRequest =
                    UserCommandUseCase.RegisterRequest(
                        username = "test_user",
                        password = "1234"
                    )
                userService.registerUser(registerRequest)

                val request =
                    UserCommandUseCase.LoginRequest(
                        username = "test_user",
                        password = "12345"
                    )

                // when
                val actual: UserCommandUseCase.LoginResponse = userService.loginUser(request)

                // then
                SoftAssertions.assertSoftly { softAssertions ->
                    softAssertions.assertThat(actual.code).isEqualTo(300)
                    softAssertions.assertThat(actual.message).isEqualTo("invalid password")
                }
            }
        }
    }
}
