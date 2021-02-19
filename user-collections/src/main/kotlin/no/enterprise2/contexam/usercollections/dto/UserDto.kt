/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/CardDto.kt
*/

package no.enterprise2.contexam.usercollections.dto

import io.swagger.annotations.ApiModelProperty

data class UserDto(

        @get:ApiModelProperty("The id of the user")
        var userId: String? = null

        /*@get:ApiModelProperty("Number of person booked on this trip")
        var person: Int? = null,

        @get:ApiModelProperty("The amount of coins owned by the user for buying and selling tickets")
        var coins: Int? = null,

        @get:ApiModelProperty("List of bookings owned by the user")
        var bookedTrips: MutableList<BookingDto> = mutableListOf()*/
)