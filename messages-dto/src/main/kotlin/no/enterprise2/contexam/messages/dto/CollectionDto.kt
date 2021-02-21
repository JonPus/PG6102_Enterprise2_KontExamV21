/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/CollectionDto.kt
*/

package no.enterprise2.contexam.messages.dto

import io.swagger.annotations.ApiModelProperty

class CollectionDto(
        @get:ApiModelProperty("All the messages in the social site")
        var messages: MutableList<MessageDto> = mutableListOf(),

        @get:ApiModelProperty("Different classes for the messages")
        var classes: MutableMap<MessageClass, Int> = mutableMapOf()

)