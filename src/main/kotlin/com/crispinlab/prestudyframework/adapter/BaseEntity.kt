package com.crispinlab.prestudyframework.adapter

import com.crispinlab.prestudyframework.common.util.SnowflakeIdCreator
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import java.time.Instant
import org.springframework.data.domain.Persistable

@MappedSuperclass
internal abstract class BaseEntity() : Persistable<Long> {
    @Id
    val id: Long = SnowflakeIdCreator.nextId()

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

    @Column(nullable = false)
    val createdBy: Long = 0L

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
