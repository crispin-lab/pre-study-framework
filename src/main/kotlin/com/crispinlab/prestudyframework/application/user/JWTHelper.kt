package com.crispinlab.prestudyframework.application.user

import com.crispinlab.prestudyframework.common.config.JWTProperties
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import org.springframework.stereotype.Component

interface JWTHelper {
    fun createJWT(userid: Long): String
}

@Component
class NimbusJWTHelper(
    private val jwtProperties: JWTProperties
) : JWTHelper {
    override fun createJWT(userid: Long): String {
        val claimsSet: JWTClaimsSet =
            JWTClaimsSet.Builder()
                .subject(userid.toString())
                .issuer("api.login")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(calculateExpiration()))
                .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)
        signedJWT.sign(MACSigner(jwtProperties.secret))

        return signedJWT.serialize()
    }

    private fun calculateExpiration(): Instant =
        Instant.now().plus(jwtProperties.expirationMinutes, ChronoUnit.MINUTES)
}
