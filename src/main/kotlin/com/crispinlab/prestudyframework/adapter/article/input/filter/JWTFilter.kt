package com.crispinlab.prestudyframework.adapter.article.input.filter

import com.crispinlab.prestudyframework.adapter.article.input.web.response.ArticleResponse
import com.crispinlab.prestudyframework.application.user.JWTHelper
import com.crispinlab.prestudyframework.common.exception.ErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class JWTFilter(
    private val jwtHelper: JWTHelper,
    private val objectMapper: ObjectMapper
) : Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse
        val jwt: String? =
            httpRequest.cookies
                ?.firstOrNull { it.name == "AUTH-TOKEN" }
                ?.value

        val allowedMethods: Set<String> = setOf("GET", "OPTIONS")

        if (httpRequest.method.equalsAnyIgnoreCase(allowedMethods)) {
            chain?.doFilter(request, response)
            return
        }

        if (jwt == null) {
            val articleResponse: ArticleResponse<Unit> =
                ArticleResponse.error(
                    code = ErrorCode.MISSING_JWT_COOKIE.code,
                    message = ErrorCode.MISSING_JWT_COOKIE.message
                )
            httpResponse.writer.write(objectMapper.writeValueAsString(articleResponse))
            return
        }

        try {
            val subject: Long = jwtHelper.parseJWT(jwt).toLong()
            httpRequest.setAttribute("userId", subject)
        } catch (_: Exception) {
            val articleResponse: ArticleResponse<Unit> =
                ArticleResponse.error(
                    code = ErrorCode.INVALID_JWT.code,
                    message = ErrorCode.INVALID_JWT.message
                )
            httpResponse.writer.write(objectMapper.writeValueAsString(articleResponse))
        }
        chain?.doFilter(httpRequest, httpResponse)
    }

    private fun String.equalsAnyIgnoreCase(candidates: Set<String>): Boolean {
        return candidates.any { it.equals(this, ignoreCase = true) }
    }
}
