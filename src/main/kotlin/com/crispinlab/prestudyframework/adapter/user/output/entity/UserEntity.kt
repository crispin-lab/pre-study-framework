package com.crispinlab.prestudyframework.adapter.user.output.entity

import com.crispinlab.prestudyframework.adapter.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
internal class UserEntity(
    @Id
    val id: Long = 0L,
    @Column(nullable = false, length = 20)
    val username: String,
    @Column(nullable = false, length = 64)
    val password: String
) : BaseEntity()
