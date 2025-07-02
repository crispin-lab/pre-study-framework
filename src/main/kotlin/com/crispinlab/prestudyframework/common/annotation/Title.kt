package com.crispinlab.prestudyframework.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [])
annotation class Title(
    val message: String = "invalid title",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
