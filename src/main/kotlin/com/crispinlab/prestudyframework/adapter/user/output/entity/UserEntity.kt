package com.crispinlab.prestudyframework.adapter.user.output.entity

import com.crispinlab.prestudyframework.adapter.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
internal class UserEntity(
    @Column(nullable = false, length = 20)
    val username: String,
    @Column(nullable = false, length = 20)
    val password: String
) : BaseEntity()
