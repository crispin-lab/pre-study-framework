package com.crispinlab.prestudyframework.common.util

import com.crispinlab.prestudyframework.common.exception.CustomException
import com.crispinlab.prestudyframework.common.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal object Log {
    fun <T : Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

    fun <T> logging(
        log: Logger,
        function: (MutableMap<String, Any>) -> T?
    ): T {
        val logInfo: MutableMap<String, Any> = mutableMapOf()

        val startedAt: Long = now()
        logInfo["startedAt"] = startedAt

        val result: T? = function.invoke(logInfo)

        val endedAt: Long = now()
        logInfo["endedAt"] = endedAt

        logInfo["timeTaken"] = measureTime(startedAt, endedAt)

        log.info(logInfo.toString())
        return result ?: throw CustomException(ErrorCode.FAILED_TO_INVOKE_IN_LOG)
    }

    fun now() = System.currentTimeMillis()

    private fun measureTime(
        startedAt: Long,
        endedAt: Long
    ): Long = endedAt - startedAt
}
