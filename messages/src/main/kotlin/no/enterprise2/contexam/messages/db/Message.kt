/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStats.kt
 */

package no.enterprise2.contexam.messages.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Message(
        @get:Id
        @get:NotBlank
        var messageId: String? = "test",

        @get:NotBlank
        var message: String? = "default",

        @get:NotBlank
        var userId: String? = null,

        @get:NotBlank
        var friendId: String? = null,

        @get:NotNull
        var uniqueNumber: Int? = null



)