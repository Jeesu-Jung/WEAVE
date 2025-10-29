package io.jeesu.grovecacheservice.infrastructure.config

import feign.Logger
import feign.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig(
    @Value("\${feign.client.config.default.connectTimeout:15000}") private val connectTimeoutMs: Int,
    @Value("\${feign.client.config.default.readTimeout:15000}") private val readTimeoutMs: Int
) {
    @Bean
    fun feignOptions(): Request.Options = Request.Options(connectTimeoutMs, readTimeoutMs)

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.BASIC
}
