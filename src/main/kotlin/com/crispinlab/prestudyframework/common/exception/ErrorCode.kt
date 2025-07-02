package com.crispinlab.prestudyframework.common.exception

internal interface CodeInterface {
    val code: Int
    var message: String
}

internal enum class ErrorCode(
    override val code: Int,
    override var message: String
) : CodeInterface {
    FAILED_TO_INVOKE_IN_LOG(code = 100, message = "failed to invoke in log."),
    INVALID_REQUEST_VALUE(code = 101, message = "invalid request value."),
    MISSING_JWT_COOKIE(code = 102, message = "missing jwt cookie"),
    INVALID_JWT(code = 103, message = "invalid jwt"),
    NOT_FOUND_SUBJECT(code = 104, message = "not found subject")
}
