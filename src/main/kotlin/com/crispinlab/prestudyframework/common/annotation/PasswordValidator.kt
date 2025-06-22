package com.crispinlab.prestudyframework.common.annotation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false
        return Regex("^[a-zA-Z0-9]{8,15}$").matches(value)
    }
}
