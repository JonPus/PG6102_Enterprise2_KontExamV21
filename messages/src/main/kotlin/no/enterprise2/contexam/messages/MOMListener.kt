/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/MOMListener.kt
*/

package no.enterprise2.contexam.messages

import no.enterprise2.contexam.messages.db.MessageService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MOMListener(
        private val messageService: MessageService
) {
    companion object {
        private val log = LoggerFactory.getLogger(MOMListener::class.java)
    }

    @RabbitListener(queues = ["#{queue.name}"])
    fun receiveFromAMQP(messageId: String, message: String, userId: String, friendId: String, uniqueNumber: Int) {
        val ok = messageService.createNewMessage(messageId, message, userId, friendId, uniqueNumber)
        if (ok) {
            log.info("Created a new message to friend via MOM: $messageId")
        }
    }
}