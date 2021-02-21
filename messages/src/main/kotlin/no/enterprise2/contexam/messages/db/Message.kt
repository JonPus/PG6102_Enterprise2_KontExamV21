package no.enterprise2.contexam.messages.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank

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
        var friendId: String? = null

)