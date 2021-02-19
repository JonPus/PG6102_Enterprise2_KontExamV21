/*
This is from Andrea Arcuri's repository:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/FakeDataService.kt
*/

package no.enterprise2.contexam.usercollections.db

import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user_data")
class User(

        @get:Id
        @get:NotBlank
        var userId: String? = null

        /*@get:Min(0)
        var person: Int = 0,

        @get:Min(0)
        var coins: Int = 0,

        @get:OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
        var bookedTrips: MutableList<Booking> = mutableListOf()*/
)