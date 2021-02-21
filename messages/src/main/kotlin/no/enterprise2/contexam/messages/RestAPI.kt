/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/RestAPI.kt
*/

package no.enterprise2.contexam.messages

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.enterprise2.contexam.messages.db.MessageRepository
import no.enterprise2.contexam.messages.db.MessageService
import no.enterprise2.contexam.messages.dto.CollectionDto
import no.enterprise2.contexam.messages.dto.MessageDto
import no.enterprise2.contexam.rest.dto.PageDto
import no.enterprise2.contexam.rest.dto.RestResponseFactory
import no.enterprise2.contexam.rest.dto.WrappedResponse
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.TimeUnit

@Api(value = "/api/messages", description = "Operation on the messages existing in the social site")
@RequestMapping(path = ["/api/messages"])
@RestController
class RestAPI(
        private val messageService: MessageService,
        private val messageRepository: MessageRepository
) {

    companion object {
        const val LATEST = "v1_000"
    }

    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange

    @ApiOperation("Return info on all the messages in the social site")
    @GetMapping(
            path = ["/collection_$LATEST"],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLatest(): ResponseEntity<WrappedResponse<CollectionDto>> {

        val collection = MessageCollection.get()

        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .body(WrappedResponse(200, collection).validated())
    }

    @ApiOperation("Old-version endpoints, Will automatically redirect to most recent version")
    @GetMapping(
            path = [
                "/collection_v0_001",
                "/collection_v0_002",
                "/collection_v0_003"
            ]
    )
    fun getOld(): ResponseEntity<Void> {

        return ResponseEntity.status(301)
                .location(URI.create("/api/messages/collection_$LATEST"))
                .build()
    }

    @ApiOperation("Retrieve the current messages based on friendID")
    @GetMapping(path = ["/{messageId}"])
    fun getMessage(@PathVariable("messageId") messageId: String): ResponseEntity<WrappedResponse<MessageDto>> {

        val message = messageRepository.findById(messageId).orElse(null)
        if (message == null) {
            return RestResponseFactory.notFound("Message $messageId not found.")
        }
        return RestResponseFactory.payload(200, DtoConverter.transform(message))
    }

    @ApiOperation("Create default a message")
    @PutMapping(path = ["/{messageId}"])
    fun createMessage(
            @RequestBody dto: MessageDto
    ): ResponseEntity<WrappedResponse<MessageDto>> {

        val messageId = dto.messageId
                ?: return RestResponseFactory.userFailure("Missing messageId")

        val ok = messageService.createNewMessage(messageId, dto.message, dto.userId, dto.friendId, dto.uniqueNumber)

        val message = messageRepository.findById(dto.messageId!!).orElse(null)

        return if (!ok) {
            RestResponseFactory.userFailure("Message ${dto.messageId} already exists")
        } else {
            template.convertAndSend(fanout.name, "", dto.messageId!!)
            RestResponseFactory.payload(201, DtoConverter.transform(message))
        }
    }

    @ApiOperation("Post method for creating messages")
    @PostMapping(path = ["/{messageId}"])
    fun createTrip2(
            @PathVariable("messageId") messageId: String, message: String?, userId: String?, friendId: String?, uniqueNumber: Int?
    ): ResponseEntity<WrappedResponse<MessageDto>> {

        val ok = messageService.createNewMessage(messageId, message, userId, friendId, uniqueNumber)

        val createdMessage = messageRepository.findById(messageId).orElse(null)

        return if (!ok) {
            RestResponseFactory.userFailure("Message $messageId already exists")
        } else {
            template.convertAndSend(fanout.name, "", messageId)
            RestResponseFactory.payload(201, DtoConverter.transform(createdMessage))
        }
    }

    @ApiOperation("Deleting given message by messageId")
    @DeleteMapping(path = ["/{messageId}"])
    fun deleteMessage(
            @PathVariable("messageId") messageId: String
    ): ResponseEntity<WrappedResponse<Void>> {

        messageService.messageRepository.deleteById(messageId)

        template.convertAndSend(fanout.name, "", messageId)
        return RestResponseFactory.noPayload(200)
    }

    @ApiOperation("Return an iterable page of list of messages, starting from friendIds per message")
    @GetMapping
    fun getAll(
            @ApiParam("Id of the message in the previous page")
            @RequestParam("keySetId", required = false)
            keySetId: String?,
            //
            @ApiParam("UniqueNumber per messages in the previous page")
            @RequestParam("keySetMessage", required = false)
            keySetMessage: Int?
    ): ResponseEntity<WrappedResponse<PageDto<MessageDto>>> {

        val page = PageDto<MessageDto>()

        val n = 10

        val messages = DtoConverter.transform(messageService.getNextTimeline(n, keySetId, keySetMessage))
        page.list = messages

        if (messages.size == n) {
            val last = messages.last()
            page.next = "/api/messages?keySetId=${last.messageId}&keySetMessage=${last.uniqueNumber}"
        }

        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
                .body(WrappedResponse(200, page).validated())
    }
}