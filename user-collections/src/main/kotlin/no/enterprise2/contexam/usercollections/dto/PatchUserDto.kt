/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/dto/PatchUserDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty

enum class Command {

    ALTER_USER,

    RETURN_FRIENDSHIP,

    ADD_FRIENDSHIP,

    DELETE_FRIENDSHIP

}

data class PatchUserDto(

        @get:ApiModelProperty("Command to execute on a user's collection")
        var command: Command? = null,

        @get:ApiModelProperty("firstName of the user")
        var firstName: String? = null,

        @get:ApiModelProperty("lastname of the user")
        var lastName: String? = null,

        @get:ApiModelProperty("id of the friend")
        var friendId: String? = null,

        @get:ApiModelProperty("email of the user")
        var email: String? = null
)