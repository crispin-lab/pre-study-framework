package com.crispinlab.prestudyframework.common.config

import com.crispinlab.Snowflake
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IdGenerateConfig {
    @Bean
    fun snowflake(): Snowflake = Snowflake.create()
}
