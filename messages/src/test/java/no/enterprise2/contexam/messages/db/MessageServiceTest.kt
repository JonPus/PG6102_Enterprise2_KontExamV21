/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/db/UserServiceTest.kt
*/

package no.enterprise2.contexam.messages.db

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("FakeData,test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class MessageServiceTest {

    @Autowired
    private lateinit var service: MessageService

    @Autowired
    private lateinit var repository: MessageRepository

    @Test
    fun testInit() {
        assertTrue(repository.count() > 0)
    }

    @Test
    fun testCreateMessage() {
        val n = repository.count()

        service.createNewMessage("m001", "Do you forgive me?", "u001", "f001", 1)
        assertEquals(n + 1, repository.count())
    }

    @Test
    fun testDeleteTrip() {

        val n = repository.count()
        val id = "m0012"

        service.createNewMessage(id, "What is love?", "u001", "u002", 2)

        assertEquals(n + 1, repository.count())

        service.deleteMessage(id)

        assertEquals(n, repository.count())
    }

    @Test
    fun testPage() {

        val n = 5
        val page = service.getNextTimeline(n)

        for (i in 0 until n - 1) {
            assertTrue(page[i].uniqueNumber!! >= page[i + 1].uniqueNumber!!)
        }

    }

}