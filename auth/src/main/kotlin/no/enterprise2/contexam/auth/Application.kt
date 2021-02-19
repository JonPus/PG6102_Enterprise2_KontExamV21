/*
*
* This ahs been taken from Andrea Arcuri's Repository page: https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/auth/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/auth/Application.kt
*
**/

package no.enterprise2.contexam.auth

import org.springframework.amqp.core.FanoutExchange
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class Application {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("user-creation")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}