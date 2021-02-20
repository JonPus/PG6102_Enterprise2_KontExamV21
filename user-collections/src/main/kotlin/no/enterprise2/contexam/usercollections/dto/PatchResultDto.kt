/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/dto/PatchUserDto.kt*/


package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty

class PatchResultDto {

    @get:ApiModelProperty("If a friendship was added, specify which friend was in it")
    var friendIdInFriendRequest: MutableList<String> = mutableListOf()
}