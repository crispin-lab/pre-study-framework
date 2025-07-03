package com.crispinlab.prestudyframework.common.config

import com.crispinlab.prestudyframework.common.util.Log
import jakarta.servlet.http.HttpServletRequest
import java.util.Optional
import org.slf4j.Logger
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class AuditorAwareImpl : AuditorAware<Long> {
    private val logger: Logger = Log.getLogger(AuditorAwareImpl::class.java)

    override fun getCurrentAuditor(): Optional<Long> =
        Log.logging(logger) { log ->
            log["method"] = "getCurrentAuditor()"
            val requestAttributes: RequestAttributes? = RequestContextHolder.getRequestAttributes()
            val request: HttpServletRequest? =
                (requestAttributes as? ServletRequestAttributes)?.request

            val userId: Long = request?.getAttribute("userId") as? Long ?: 0L
            log["userId"] = userId.toString()
            return@logging Optional.ofNullable(userId)
        }
}
