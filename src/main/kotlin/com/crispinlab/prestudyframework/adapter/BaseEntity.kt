package com.crispinlab.prestudyframework.adapter

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import java.time.Instant
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
internal abstract class BaseEntity() : Persistable<Long> {
    @Column(nullable = false)
    val createdAt: Instant = Instant.now()

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

    @CreatedBy
    @Column(nullable = false)
    var createdBy: Long = 0L

    @LastModifiedBy
    @Column(nullable = false)
    var updatedBy: Long = 0L

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
