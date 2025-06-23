package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.adapter.user.input.web.AuthHeaderBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie

class AuthHeaderFakeBuilder : AuthHeaderBuilder {
    override fun buildHeader(token: String): HttpHeaders =
        HttpHeaders().apply {
            add(HttpHeaders.AUTHORIZATION, "Bearer $token")
            add(HttpHeaders.SET_COOKIE, buildAuthCookie(token).toString())
        }

    private fun buildAuthCookie(token: String): ResponseCookie =
        ResponseCookie.from("AUTH-TOKEN", token)
            .httpOnly(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(3600)
            .build()
}
