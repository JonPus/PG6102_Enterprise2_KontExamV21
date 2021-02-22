/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/FakeData.kt
*/

package no.enterprise2.contexam.usercollections

import no.enterprise2.contexam.messages.dto.CollectionDto
import no.enterprise2.contexam.messages.dto.MessageClass.*
import no.enterprise2.contexam.messages.dto.MessageDto

object FakeData {

    fun getCollectionDto(): CollectionDto {

        val dto = CollectionDto()

        dto.classes[POST] = 1
        dto.classes[MESSAGE] = 2
        dto.classes[PRIVATE_MESSAGE] = 3
        dto.classes[PUBLIC_MESSAGE] = 4

        dto.messages.run {

            add(MessageDto(messageId = "m00", messageClass = POST))
            add(MessageDto(messageId = "m01", messageClass = POST))
            add(MessageDto(messageId = "m02", messageClass = POST))
            add(MessageDto(messageId = "m03", messageClass = POST))
            add(MessageDto(messageId = "m04", messageClass = MESSAGE))
            add(MessageDto(messageId = "m05", messageClass = MESSAGE))
            add(MessageDto(messageId = "m06", messageClass = MESSAGE))
            add(MessageDto(messageId = "m07", messageClass = PRIVATE_MESSAGE))
            add(MessageDto(messageId = "m08", messageClass = PRIVATE_MESSAGE))
            add(MessageDto(messageId = "m09", messageClass = PUBLIC_MESSAGE))
        }
        return dto
    }
}