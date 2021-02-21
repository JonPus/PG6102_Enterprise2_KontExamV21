/*
This is from Andrea Arcuri's repository:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/FakeDataService.kt
 */

package no.enterprise2.contexam.messages.db

import com.github.javafaker.Faker
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Profile("FakeData")
@Service
@Transactional
class FakeDataService(
        val repository: MessageRepository
) {

    private val faker = Faker()

    @PostConstruct
    fun init() {
        for (i in 0..49) {
            createRandomMessages("Foo" + i.toString().padStart(2, '0'))
        }
    }

    private fun createRandomMessages(messageId: String) {

        val details = Message(
                messageId,
                faker.chuckNorris().fact(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().numberBetween(1, 1000)
        )
        repository.save(details)
    }
}