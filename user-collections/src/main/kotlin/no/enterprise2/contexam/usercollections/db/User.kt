/*
This is from Andrea Arcuri's repository:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/FakeDataService.kt
*/

package no.enterprise2.contexam.usercollections.db

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user_data")
class User(

        @get:Id
        @get:NotBlank
        var userId: String? = null,

        @get:NotBlank
        var firstName: String? = null,

        @get:NotBlank
        var lastName: String? = null,

        @get:NotBlank
        @get:Email
        var email: String? = null,

        @get:OneToMany(mappedBy = "user1", cascade = [(CascadeType.ALL)])
        var friendList: MutableList<Friendship> = mutableListOf()

)