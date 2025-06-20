package com.crispinlab.prestudyframework

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.crispinlab.prestudyframework.common.config")
class PreStudyFrameworkApplication

fun main(args: Array<String>) {
    runApplication<PreStudyFrameworkApplication>(*args)
}
