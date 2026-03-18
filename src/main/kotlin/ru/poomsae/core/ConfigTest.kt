package ru.poomsae.core

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class ConfigTest(private val env: Environment) {
    
    @Bean
    fun checkConfig(): CommandLineRunner {
        return CommandLineRunner {
            println("=== CONFIGURATION DEBUG ===")
            println("CORE_POSTGRES_HOST: ${env.getProperty("CORE_POSTGRES_HOST", "NOT_FOUND")}")
            println("CORE_POSTGRES_PORT: ${env.getProperty("CORE_POSTGRES_PORT", "NOT_FOUND")}")
            println("CORE_POSTGRES_DB: ${env.getProperty("CORE_POSTGRES_DB", "NOT_FOUND")}")
            println("CORE_POSTGRES_USER: ${env.getProperty("CORE_POSTGRES_USER", "NOT_FOUND")}")
            println("CORE_POSTGRES_PASSWORD: ${env.getProperty("CORE_POSTGRES_PASSWORD", "NOT_FOUND")}")
            println("=== END DEBUG ===")
        }
    }
}