package no.enterprise2.contexam.usercollections

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("user-creation")
    }

    @Bean
    fun queue(): Queue {
        return Queue("user-creation-user-collections")
    }

    @Bean
    fun binding(
            fanout: FanoutExchange,
            queue: Queue
    ): Binding {
        return BindingBuilder.bind(queue).to(fanout)
    }

}