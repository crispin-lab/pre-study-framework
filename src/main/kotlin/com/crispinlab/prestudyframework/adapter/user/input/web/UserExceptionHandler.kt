package com.crispinlab.prestudyframework.adapter.user.input.web

import com.crispinlab.prestudyframework.adapter.user.input.web.response.UserResponse
import com.crispinlab.prestudyframework.common.exception.ErrorCode
import com.crispinlab.prestudyframework.common.util.Log
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@ResponseStatus(value = HttpStatus.OK)
@RestControllerAdvice
internal class UserExceptionHandler {
    private val logger: Logger = Log.getLogger(UserExceptionHandler::class.java)

    data class ValidationErrors(
        val errors: List<ValidationError>
    )

    data class ValidationError(
        val field: String,
        val value: Any?
    ) {
        companion object {
            fun of(filedError: FieldError): ValidationError =
                ValidationError(filedError.field, filedError.rejectedValue)
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandle(
        exception: MethodArgumentNotValidException
    ): UserResponse<ValidationErrors> =
        Log.warnLogging(logger) { log ->
            val errors: List<ValidationError> =
                exception.bindingResult.fieldErrors
                    .stream()
                    .map(ValidationError::of)
                    .toList()

            log["message"] = exception.message

            return@warnLogging UserResponse.fail(
                code = ErrorCode.INVALID_REQUEST_VALUE.code,
                message = ErrorCode.INVALID_REQUEST_VALUE.message,
                result = ValidationErrors(errors)
            )
        }
}
