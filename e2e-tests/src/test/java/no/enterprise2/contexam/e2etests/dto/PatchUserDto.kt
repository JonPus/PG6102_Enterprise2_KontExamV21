package no.enterprise2.contexam.e2etests.dto

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

        @get:ApiModelProperty("Friend ID to find and add to friend list")
        var friendId: String? = null,

        @get:ApiModelProperty("Accept or Decline friend request")
        var status: Int = 1
)