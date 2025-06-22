package com.crispinlab.prestudyframework.fake

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "test_user_table")
class UserFakeEntity(
    @Id
    val id: Long = 0L,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val password: String
)

@Entity
@Table(name = "test_user_table2")
class UserFakeEntity2(
    @Id
    val id: Long = 0L,
    @Column(nullable = false)
    val username: String,
    @Column(nullable = false)
    val password: String
) : Persistable<Long> {
    @Transient
    private var isNew = true

    override fun isNew(): Boolean = isNew

    override fun getId(): Long? = id

    @PrePersist
    @PostLoad
    private fun markNotNew() {
        isNew = false
    }
}

interface UserFakeRepository : JpaRepository<UserFakeEntity, Long>

interface UserFakeRepository2 : JpaRepository<UserFakeEntity2, Long>
