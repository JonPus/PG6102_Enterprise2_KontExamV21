package no.enterprise2.contexam.auth.db

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Profile("adminUser")
@Service
@Transactional
class InitUserConstruct(
        val userService: UserService
) {

    @PostConstruct
    fun init() {
        userService.createUser("adminAMQP", "admin", setOf("ADMIN", "USER"))
        userService.createUser("userAMQP", "user", setOf("USER"))
    }
}