package com.crispinlab.prestudyframework.common.config

import com.crispinlab.prestudyframework.adapter.article.input.filter.JWTFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun jwtFilterRegistration(jwtFilter: JWTFilter): FilterRegistrationBean<JWTFilter> {
        val filterRegistrationBean: FilterRegistrationBean<JWTFilter> =
            FilterRegistrationBean(jwtFilter)
        filterRegistrationBean.addUrlPatterns("/api/article/*")
        filterRegistrationBean.addUrlPatterns("/api/articles/*")
        filterRegistrationBean.order = 1
        return filterRegistrationBean
    }
}
