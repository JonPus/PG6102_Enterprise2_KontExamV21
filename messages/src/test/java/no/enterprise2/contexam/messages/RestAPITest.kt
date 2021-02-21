/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/RestAPITest.kt
*/

package no.enterprise2.contexam.messages

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import no.enterprise2.contexam.messages.RestAPI.Companion.LATEST
import no.enterprise2.contexam.messages.db.MessageRepository
import no.enterprise2.contexam.messages.db.MessageService
import no.enterprise2.contexam.messages.dto.MessageDto
import no.enterprise2.contexam.rest.dto.PageDto
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import javax.annotation.PostConstruct

@ActiveProfiles("FakeData,test")
@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(
        classes = [(Application::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [(RestAPITest.Companion.Initializer::class)])
internal class RestAPITest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @Container
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues
                        .of(
                                "spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                                "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672)
                        )
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var repository: MessageRepository

    @Autowired
    private lateinit var service: MessageService

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testCreateMessage() {

        val messageId = "m001"
        val message = "Runaway with me?"
        val userId = "u001"
        val friendId = "u002"
        val uniqueNumber = 1

        RestAssured.given().auth().basic("admin", "admin").contentType(ContentType.JSON)
                .body(
                        MessageDto(messageId, message, userId, friendId, uniqueNumber)
                )
                .put("/api/messages/$messageId")
                .then()
                .statusCode(201)

        assertTrue(repository.existsById(messageId))

        RestAssured.given().auth().basic("admin", "admin").contentType(ContentType.JSON)
                .get("/api/messages/$messageId")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString(messageId))
                .body(CoreMatchers.containsString(message))
                .body(CoreMatchers.containsString(userId))
                .body(CoreMatchers.containsString(friendId))
    }

    @Test
    fun testDeleteMessage() {

        val messageId = "m0003"
        val message = "Head above water"
        val userId = "u001"
        val friendId = "u002"
        val uniqueNumber = 1

        RestAssured.given().auth().basic("admin", "admin").contentType(ContentType.JSON)
                .body(
                        MessageDto(messageId, message, userId, friendId, uniqueNumber)
                )
                .put("/api/messages/$messageId")
                .then()
                .statusCode(201)

        assertTrue(repository.existsById(messageId))

        RestAssured.given().auth().basic("admin", "admin")
                .delete("/api/messages/$messageId")
                .then()
                .statusCode(200)

        assertFalse(repository.existsById(messageId))
    }

    @Test
    fun testGetCollection() {

        RestAssured.given().get("/api/messages/collection_$LATEST")
                .then()
                .statusCode(200)
                .body("data.messages.size", Matchers.greaterThan(9))
    }

    @Test
    fun testGetCollectionOldVersion() {

        RestAssured.given().get("/api/messages/collection_v0_002")
                .then()
                .statusCode(200)
                .body("data.messages.size", Matchers.greaterThan(9))
    }

    val page: Int = 10

    @Test
    fun testGetPage() {

        RestAssured.given().accept(ContentType.JSON)
                .get("/api/messages")
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(page))
    }

    @Test
    fun testAllPages() {

        val read = mutableSetOf<String>()

        var page = RestAssured.given().accept(ContentType.JSON)
                .get("/api/messages")
                .then()
                .statusCode(200)
                .body("data.list.size", CoreMatchers.equalTo(page))
                .extract().body().jsonPath()
                .getObject("data", object : TypeRef<PageDto<Map<String, Object>>>() {})
        read.addAll(page.list.map { it["messageId"].toString() })

        checkOrder(page)

        while (page.next != null) {

            page = RestAssured.given().accept(ContentType.JSON)
                    .get(page.next)
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath()
                    .getObject("data", object : TypeRef<PageDto<Map<String, Object>>>() {})
            read.addAll(page.list.map { it["messageId"].toString() })
            checkOrder(page)
        }

        val total = repository.count().toInt()
        println(total)
        println(read.size)

        assertEquals(total, read.size)
    }

    private fun checkOrder(page: PageDto<Map<String, Object>>) {

        for (i in 0 until page.list.size - 1) {
            val ascore = page.list[i]["uniqueNumber"].toString().toInt()
            val bscore = page.list[i + 1]["uniqueNumber"].toString().toInt()
            val aid = page.list[i]["messageId"].toString()
            val bid = page.list[i + 1]["messageId"].toString()
            assertTrue(ascore >= bscore)
            if (ascore == bscore) {
                assertTrue(aid > bid)
            }
        }
    }

    @Test
    fun testAccessControl() {

        val id = "m006"

        service.createNewMessage(id, "Still alive", "u001", "u002", 1)


        val message = repository.findById(id).get()

        assertEquals("m006", message.messageId)

        RestAssured.given().get("/api/messages/$id").then().statusCode(200)
        RestAssured.given().put("/api/messages/$id").then().statusCode(401)
        RestAssured.given().patch("/api/messages/$id").then().statusCode(401)
        RestAssured.given().delete("/api/messages/$id").then().statusCode(401)

        RestAssured.given().auth().basic("admin", "admin")
                .get("/api/messages/$id")
                .then()
                .statusCode(200)
    }
}