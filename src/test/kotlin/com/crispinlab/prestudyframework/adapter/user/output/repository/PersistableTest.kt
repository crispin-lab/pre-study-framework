package com.crispinlab.prestudyframework.adapter.user.output.repository

import com.crispinlab.prestudyframework.fake.UserFakeEntity
import com.crispinlab.prestudyframework.fake.UserFakeEntity2
import com.crispinlab.prestudyframework.fake.UserFakeRepository
import com.crispinlab.prestudyframework.fake.UserFakeRepository2
import org.assertj.core.api.Assertions
import org.hibernate.Session
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class PersistableTest {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userFakeRepository: UserFakeRepository

    @Autowired
    private lateinit var userFakeRepository2: UserFakeRepository2

    @Test
    @DisplayName("Persistable 구현체를 사용하지 않았을때")
    fun nonPersistableTest() {
        val stats: Statistics =
            entityManager.entityManager
                .unwrap(Session::class.java)
                .sessionFactory
                .statistics.also {
                    it.isStatisticsEnabled = true
                    it.clear()
                }

        val entity = UserFakeEntity(id = 1L, username = "test_user", password = "1234")
        userFakeRepository.save(entity)
        userFakeRepository.flush()

        val queryCount: Long = stats.prepareStatementCount
        println("Query Count: $queryCount")

        Assertions.assertThat(queryCount).isGreaterThanOrEqualTo(2)
    }

    @Test
    @DisplayName("Persistable 구현체를 사용했들때")
    fun persistableTest() {
        val stats: Statistics =
            entityManager.entityManager
                .unwrap(Session::class.java)
                .sessionFactory
                .statistics.also {
                    it.isStatisticsEnabled = true
                    it.clear()
                }

        val entity = UserFakeEntity2(id = 1L, username = "test_user", password = "1234")
        userFakeRepository2.save(entity)
        userFakeRepository2.flush()

        val queryCount: Long = stats.prepareStatementCount
        println("Query Count: $queryCount")

        Assertions.assertThat(queryCount).isGreaterThanOrEqualTo(1)
    }
}
