/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/DtoConverter.kt
*/


package no.enterprise2.contexam.usercollections

import no.enterprise2.contexam.usercollections.db.User
import no.enterprise2.contexam.usercollections.dto.UserDto

object DtoConverter {

    fun transform(user: User): UserDto {

        return UserDto().apply {
            userId = user.userId
            firstName = user.firstName
            lastName = user.lastName
            email = user.email

        }
    }
}