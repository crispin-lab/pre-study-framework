package com.crispinlab.prestudyframework.fake

import com.crispinlab.prestudyframework.application.user.JWTHelper
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class JWTFakeHelper : JWTHelper {
    override fun createJWT(userid: Long): String {
        val secret = "Z3Vlc3QtYW55LXRoaW5nLWxvbmdlci10aGFuLXR3ZW50eS1jaGFyYWN0ZXJz"
        val claimsSet: JWTClaimsSet =
            JWTClaimsSet.Builder()
                .subject(userid.toString())
                .issuer("api.login.test")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(calculateExpiration()))
                .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)
        signedJWT.sign(MACSigner(secret))

        return signedJWT.serialize()
    }

    private fun calculateExpiration(): Instant = Instant.now().plus(30, ChronoUnit.MINUTES)
}
