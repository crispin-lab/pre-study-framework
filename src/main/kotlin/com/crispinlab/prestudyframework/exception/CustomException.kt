package com.crispinlab.prestudyframework.exception

internal class CustomException(
    codeInterface: CodeInterface,
    additionalMessage: String? = null
) : RuntimeException(
        additionalMessage?.let { "${codeInterface.message} - $it" }
            ?: codeInterface.message
    )
