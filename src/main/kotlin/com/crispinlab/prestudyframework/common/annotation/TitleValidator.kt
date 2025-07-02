package com.crispinlab.prestudyframework.common.annotation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class TitleValidator : ConstraintValidator<Title, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean = value == null || value.trim().isNotEmpty() && value.length <= 150
}
