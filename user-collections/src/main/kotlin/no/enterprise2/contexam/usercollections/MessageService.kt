/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsService.kt
*/

package no.enterprise2.contexam.usercollections

import no.enterprise2.contexam.messages.dto.CollectionDto
import no.enterprise2.contexam.rest.dto.WrappedResponse
import no.enterprise2.contexam.usercollections.model.Collection
import no.enterprise2.contexam.usercollections.model.Message
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.annotation.PostConstruct

@Service
class MessageService(
        private val client: RestTemplate,
        private val circuitBreakerFactory: Resilience4JCircuitBreakerFactory
) {

    companion object {
        private val log = LoggerFactory.getLogger(MessageService::class.java)
    }

    protected var collection: Collection? = null

    @Value("\${messageServiceAddress}")
    private lateinit var messageServiceAddress: String

    val messageCollection: List<Message>
        get() = collection?.messages ?: listOf()

    private val lock = Any()

    private lateinit var cb: CircuitBreaker

    fun isInitialized() = messageCollection.isNotEmpty()

    @PostConstruct
    fun init() {

        cb = circuitBreakerFactory.create("circuitBreakerToMessages")

        synchronized(lock) {
            if (messageCollection.isNotEmpty())
                return
        }
        fetchData()
    }

    protected fun fetchData() {

        val version = "v1_000"
        val uri = UriComponentsBuilder
                .fromUriString("http://${messageServiceAddress.trim()}/api/messages/collection_$version")
                .build()
                .toUri()

        val response = cb.run(
                {
                    client.exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            object : ParameterizedTypeReference<WrappedResponse<CollectionDto>>() {})
                },
                { e ->
                    log.error("Failed to fetch Data from Message service: ${e.message}")
                    null
                }
        ) ?: return

        if (response.statusCodeValue != 200) {

            log.error(
                    "Error in fetching data from Message Service. Status ${response.statusCodeValue}." + "Message: " + response.body.message
            )
        }

        try {
            collection = Collection(response.body.data!!)
        } catch (e: Exception) {
            log.error("Failed to parse Message collection detail: ${e.message}")
        }
    }

    private fun verifyCollection() {

        if (collection == null) {
            fetchData()

            if (collection == null) {
                throw IllegalStateException("No collection info")
            }
        }
    }
}