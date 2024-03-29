package no.enterprise2.contexam.apigateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class Application {
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}