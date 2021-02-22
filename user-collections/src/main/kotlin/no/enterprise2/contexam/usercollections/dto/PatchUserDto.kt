/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/dto/PatchUserDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty
import no.enterprise2.contexam.usercollections.db.Friendship
import no.enterprise2.contexam.usercollections.db.User
import no.enterprise2.contexam.usercollections.model.Message

enum class Command {

    ALTER_USER,

    RETURN_FRIENDSHIP,

    ADD_FRIENDSHIP,

    DELETE_FRIENDSHIP

}

data class PatchUserDto(

        @get:ApiModelProperty("Command to execute on a user's collection")
        var command: Command? = null,

        @get:ApiModelProperty("Friend ID to find and add to friend list")
        var friendId: String? = null,

        @get:ApiModelProperty("Accept or Decline friend request")
        var status: Int = 1,

        @get:ApiModelProperty("List of friendships")
        var friendList: MutableList<Friendship> = mutableListOf()

)