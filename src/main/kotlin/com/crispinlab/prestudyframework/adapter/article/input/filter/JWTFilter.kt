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

        if (jwt == null) {
            val response: ArticleResponse<Unit> =
                ArticleResponse.error(
                    code = ErrorCode.MISSING_JWT_COOKIE.code,
                    message = ErrorCode.MISSING_JWT_COOKIE.message
                )
            httpResponse.writer.write(objectMapper.writeValueAsString(response))
            return
        }

        try {
            val subject: String = jwtHelper.parseJWT(jwt)
            httpRequest.setAttribute("userId", subject)
            chain?.doFilter(httpRequest, httpResponse)
        } catch (_: Exception) {
            val response: ArticleResponse<Unit> =
                ArticleResponse.error(
                    code = ErrorCode.INVALID_JWT.code,
                    message = ErrorCode.INVALID_JWT.message
                )
            httpResponse.writer.write(objectMapper.writeValueAsString(response))
        }
    }
}
