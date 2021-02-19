/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/dto/PatchUserDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty

enum class Command {

    ALTER_PERSON,

    RETURN_BOOKING,

    BUY_BOOKING,

    CANCEL_BOOKING

}

data class PatchUserDto(

        @get:ApiModelProperty("Command to execute on a user's collection")
        var command: Command? = null

        /*@get:ApiModelProperty("Id of the trip")
        var tripId: String? = null,

        @get:ApiModelProperty("The place")
        var place: String? = null,

        @get:ApiModelProperty("The amount of person for the booking a trip")
        var person: Int = 0,

        @get:ApiModelProperty("The default status of the booking")
        var isActive: Int = 1*/
)