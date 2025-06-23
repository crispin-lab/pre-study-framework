package com.crispinlab.prestudyframework.adapter.user.input.web

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

internal interface AuthHeaderBuilder {
    fun buildHeader(token: String): HttpHeaders
}

@Component
internal class AuthTokenHeaderBuilder : AuthHeaderBuilder {
    override fun buildHeader(token: String) =
        HttpHeaders().apply {
            add(HttpHeaders.AUTHORIZATION, "Bearer $token")
            add(HttpHeaders.SET_COOKIE, buildAuthCookie(token).toString())
        }

    private fun buildAuthCookie(token: String): ResponseCookie =
        ResponseCookie
            .from("AUTH-TOKEN", token)
            .httpOnly(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(3600)
            .build()
}
