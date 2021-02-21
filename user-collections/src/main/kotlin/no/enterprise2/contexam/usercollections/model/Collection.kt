/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/model/Collection.kt
*/

package no.enterprise2.contexam.usercollections.model

import no.enterprise2.contexam.messages.dto.CollectionDto

data class Collection(

        val messages: List<Message>
) {
    constructor(dto: CollectionDto) : this(

            dto.messages.map { Message(it) }
    )
}