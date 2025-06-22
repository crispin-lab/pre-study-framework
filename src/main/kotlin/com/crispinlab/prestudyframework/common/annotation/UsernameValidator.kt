package com.crispinlab.prestudyframework.common.annotation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UsernameValidator : ConstraintValidator<Username, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false
        return Regex("^[a-z0-9]{4,10}$").matches(value)
    }
}
