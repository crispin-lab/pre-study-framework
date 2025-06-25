package com.crispinlab.prestudyframework.adapter.user.output

import com.crispinlab.prestudyframework.adapter.user.output.entity.UserEntity
import com.crispinlab.prestudyframework.domain.user.User

internal fun UserEntity.toDomain(): User =
    User(
        id = this.id,
        username = this.username,
        password = this.password
    )
