package com.crispinlab.prestudyframework.integration

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import jakarta.persistence.metamodel.EntityType
import java.util.stream.Collectors
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DatabaseCleanup : InitializingBean {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var tableNames: MutableList<String>

    override fun afterPropertiesSet() {
        val entities: MutableSet<EntityType<*>> = entityManager.metamodel.entities
        tableNames =
            entities
                .stream()
                .filter { isEntity(it) && hasTableAnnotation(it) }
                .map {
                    val tableName: String = it.javaType.getAnnotation(Table::class.java).name
                    tableName.ifBlank { tableName.toSnakeCase() }
                }.collect(Collectors.toList())

        val entityNames: MutableList<String> =
            entities
                .stream()
                .filter { isEntity(it) && !hasTableAnnotation(it) }
                .map { it.name.toSnakeCase() }
                .toList()

        tableNames.addAll(entityNames)
    }

    private fun isEntity(entity: EntityType<*>): Boolean =
        entity.javaType.isAnnotationPresent(Entity::class.java)

    private fun hasTableAnnotation(entity: EntityType<*>): Boolean =
        entity.javaType.isAnnotationPresent(Table::class.java)

    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()

        for (tableName: String in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager
                .createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate()
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
}

private fun String.toSnakeCase(): String =
    this
        .replace(Regex("([a-z])([A-Z])"), "$1_$2")
        .lowercase()
