/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/RestAPITest.kt
*/


package no.enterprise2.contexam.usercollections

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.http.ContentType
import no.enterprise2.contexam.rest.dto.WrappedResponse
import no.enterprise2.contexam.usercollections.db.UserRepository
import no.enterprise2.contexam.usercollections.db.UserService
import no.enterprise2.contexam.usercollections.dto.Command
import no.enterprise2.contexam.usercollections.dto.PatchUserDto
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
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
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = [(RestAPITest.Companion.Initializer::class)])
class RestAPITest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange


    companion object {

        private lateinit var wiremockServer: WireMockServer

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @Container
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)


        @BeforeAll
        @JvmStatic
        fun initClass() {
            wiremockServer = WireMockServer(
                    WireMockConfiguration.wireMockConfig().dynamicPort().notifier(
                            ConsoleNotifier(true)
                    )
            )
            wiremockServer.start()

            val dto = WrappedResponse(code = 200, data = FakeData.getCollectionDto()).validated()
            val json = ObjectMapper().writeValueAsString(dto)

            wiremockServer.stubFor(
                    WireMock.get(WireMock.urlMatching("/api/messages/collection_.*"))
                            .willReturn(
                                    WireMock.aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json; charset=utf-8")
                                            .withBody(json)
                            )
            )
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of("tripServiceAddress: localhost:${wiremockServer.port()}")
                        .applyTo(configurableApplicationContext.environment)
                TestPropertyValues
                        .of(
                                "spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                                "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672)
                        )
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/user-collections"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @BeforeEach
    fun initTest() {
        userRepository.deleteAll()
    }

    @Test
    fun testAccessControl() {

        val id = "foo"

        userService.registerNewUser(id)

        val user = userRepository.findById(id).get()

        Assertions.assertEquals(user.userId, id)

        RestAssured.given().get("/$id").then().statusCode(401)
        RestAssured.given().put("/$id").then().statusCode(401)
        RestAssured.given().patch("/$id").then().statusCode(401)
        RestAssured.given().delete("/$id").then().statusCode(401)

        RestAssured.given().auth().basic("foo", "123")
                .get("/$id")
                .then()
                .statusCode(200)
    }

    @Test
    fun testGetUser() {

        val id = "foo"
        userService.registerNewUser(id)

        RestAssured.given().auth().basic(id, "123")
                .get("/$id")
                .then()
                .statusCode(200)
    }

    @Test
    fun testCreateUser() {
        val id = "foo"

        RestAssured.given().auth().basic(id, "123")
                .put("/$id")
                .then()
                .statusCode(201)

        assertTrue(userRepository.existsById(id))
    }

    @Test
    fun addFriendship() {

        val userId1 = "foo"
        val userId2 = "bar"

        RestAssured.given().auth().basic(userId1, "123").put("/$userId1").then().statusCode(201)
        RestAssured.given().auth().basic(userId2, "123").put("/$userId2").then().statusCode(201)

        assertTrue(userRepository.existsById(userId1))
        assertTrue(userRepository.existsById(userId2))

        val user1 = userService.findIdByEager(userId1)
        val user2 = userService.findIdByEager(userId2)

        RestAssured.given().auth().basic(userId1, "123")
                .contentType(ContentType.JSON)
                .body(PatchUserDto(Command.ADD_FRIENDSHIP, user1, user2, userId2, 1))
                .patch("/$userId1")
                .then()
                .statusCode(200)

        val user = userService.findIdByEager(userId1)!!

        println(user.friendList.size)
        println(user.userId)

        assertTrue(user.friendList.any { it.friendId == userId2 })
    }

    @Test
    fun testReceive() {

        val n = RestAssured.given()
                .get("/rabbit")
                .then()
                .statusCode(200)
                .extract().body().`as`(Int::class.java)

        val msg = "foo"

        template.convertAndSend(fanout.name, "", msg)

        Awaitility.await().atMost(3, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    RestAssured.given()
                            .port(port)
                            .get("/rabbit")
                            .then()
                            .statusCode(200)
                            .body(CoreMatchers.equalTo("${n}"))
                    true
                }
    }

}