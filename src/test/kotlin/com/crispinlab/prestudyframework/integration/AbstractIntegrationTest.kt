package com.crispinlab.prestudyframework.integration

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort

@IntegrationTest
abstract class AbstractIntegrationTest {
    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @LocalServerPort
    private var port: Int = 0

    @BeforeAll
    fun initRestAssuredPort() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
            databaseCleanup.afterPropertiesSet()
        }
    }

    @BeforeEach
    fun resetDatabase() {
        databaseCleanup.execute()
    }
}
