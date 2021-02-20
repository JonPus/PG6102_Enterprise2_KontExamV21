/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/CardDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty

data class FriendshipDto(

        @get:ApiModelProperty("Id of the post")
        var postId: String? = null,

        @get:ApiModelProperty("Number of friendships that the user have")
        var numberOfFriendship: Int? = null,

        @get:ApiModelProperty("Status of the friendship, if accepted or declined.")
        var status: Int? = null
)