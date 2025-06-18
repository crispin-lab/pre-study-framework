package com.crispinlab.prestudyframework.exception

internal interface CodeInterface {
    val code: Int
    var message: String
}

internal enum class ErrorCode(
    override val code: Int,
    override var message: String
) : CodeInterface {
    FAILED_TO_INVOKE_IN_LOG(code = 100, message = "failed to invoke in log.")
}
