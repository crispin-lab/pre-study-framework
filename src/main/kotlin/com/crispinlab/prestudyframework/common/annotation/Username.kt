package com.crispinlab.prestudyframework.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UsernameValidator::class])
annotation class Username(
    val message: String = "invalid username",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
