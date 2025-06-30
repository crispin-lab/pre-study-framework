package com.crispinlab.prestudyframework.application.user

import com.crispinlab.prestudyframework.common.config.JWTProperties
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import org.springframework.stereotype.Component

interface JWTHelper {
    fun createJWT(userid: Long): String

    fun parseJWT(jwt: String): String
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

    override fun parseJWT(jwt: String): String {
        val signedJWT: SignedJWT = SignedJWT.parse(jwt)
        val verifier = MACVerifier(jwtProperties.secret)
        signedJWT.verify(verifier)
        return signedJWT.jwtClaimsSet.subject
    }

    private fun calculateExpiration(): Instant =
        Instant.now().plus(jwtProperties.expirationMinutes, ChronoUnit.MINUTES)
}
