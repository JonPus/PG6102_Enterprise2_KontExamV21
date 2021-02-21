/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/cards/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/CardCollection.kt
*/


package no.enterprise2.contexam.messages

import no.enterprise2.contexam.messages.dto.CollectionDto
import no.enterprise2.contexam.messages.dto.MessageClass.*
import no.enterprise2.contexam.messages.dto.MessageDto

object MessageCollection {

    fun get(): CollectionDto {

        val dto = CollectionDto()

        dto.classes.run {
            put(POST, 1)
            put(MESSAGE, 2)
            put(PRIVATE_MESSAGE, 3)
            put(PUBLIC_MESSAGE, 4)
        }
        addMessages(dto)

        return dto
    }

    private fun addMessages(dto: CollectionDto) {

        dto.messages.run {
            add(MessageDto("m001", "I just got my licence everyone!", "u001", "f001", POST))
            add(MessageDto("m002", "Selling my old car!", "u001", "f001", POST))
            add(MessageDto("m003", "Anyone drinking tonight?", "u001", "f001", POST))
            add(MessageDto("m004", "Lost my phone! - User sent from his iphone", "u001", "f001", POST))
            add(MessageDto("m005", "Wanna hang out?", "u002", "f002", MESSAGE))
            add(MessageDto("m006", "I know a place", "u002", "f002", MESSAGE))
            add(MessageDto("m007", "Having a great time!", "u002", "f002", MESSAGE))
            add(MessageDto("m008", "I love you...", "u003", "f003", PRIVATE_MESSAGE))
            add(MessageDto("m009", "My heart aches", "u003", "f003", PRIVATE_MESSAGE))
            add(MessageDto("m010", "PUBLIC ANNOUNCEMENT: EARTHQUAKE! HIDE!", "u004", "f004", PUBLIC_MESSAGE))
        }

        assert(dto.messages.size == dto.messages.map { it.messageId }.toSet().size)
        assert(dto.messages.size == dto.messages.map { it.messageClass }.toSet().size)
    }
}