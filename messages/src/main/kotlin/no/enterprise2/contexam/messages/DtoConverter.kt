/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/DtoConverter.kt
*/


package no.enterprise2.contexam.messages

import no.enterprise2.contexam.messages.db.Message
import no.enterprise2.contexam.messages.dto.MessageDto

object DtoConverter {

    fun transform(transformMessage: Message): MessageDto =

            transformMessage.run { MessageDto(messageId, message, userId, friendId, uniqueNumber) }

    fun transform(messages: Iterable<Message>): List<MessageDto> = messages.map { transform(it) }

}