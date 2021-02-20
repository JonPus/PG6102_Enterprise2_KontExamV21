/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/CardDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty

data class UserDto(

        @get:ApiModelProperty("The id of the user")
        var userId: String? = null,

        @get:ApiModelProperty("first name of the user")
        var firstName: String? = null,

        @get:ApiModelProperty("last name of the user")
        var lastName: String? = null,

        @get:ApiModelProperty("email of the user")
        var email: String? = null,

        @get:ApiModelProperty("List of friendships connected by the user")
        var friendList: MutableList<FriendshipDto> = mutableListOf()
)