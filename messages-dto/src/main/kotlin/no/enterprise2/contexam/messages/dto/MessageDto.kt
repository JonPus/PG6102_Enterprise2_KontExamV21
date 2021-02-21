/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/CardDto.kt
*/

package no.enterprise2.contexam.messages.dto

import io.swagger.annotations.ApiModelProperty

data class MessageDto(

        @get:ApiModelProperty("The id of the message")
        var messageId: String? = null,

        @get:ApiModelProperty("The message in the message to the poster")
        var message: String? = null,

        @get:ApiModelProperty("userId of the creator of the message")
        var userId: String? = null,

        @get:ApiModelProperty("the receiver of the message Id")
        var friendId: String? = null,

        @get:ApiModelProperty("Unique number for sorting message")
        var uniqueNumber: Int? = null,

        @get:ApiModelProperty("The class of the message to determine where it should go to the timeline or private inbox of user.")
        var messageClass: MessageClass? = null

)