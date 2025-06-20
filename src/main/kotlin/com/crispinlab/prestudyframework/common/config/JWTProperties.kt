package com.crispinlab.prestudyframework.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JWTProperties(
    val secret: String,
    val expirationMinutes: Long
)
