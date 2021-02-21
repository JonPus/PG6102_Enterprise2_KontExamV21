package no.enterprise2.contexam.usercollections.db


import no.enterprise2.contexam.messages.dto.MessageDto
import no.enterprise2.contexam.usercollections.model.Message
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Friendship(

        @get:Id
        @get:GeneratedValue
        var id: Long? = null,

        @get:ManyToOne
        @get:NotNull
        var user1: User? = null,

        @get:ManyToOne
        @get:NotNull
        var user2: User? = null,

        @get:NotBlank
        var friendId: String? = null,

        @get:Min(0)
        var numberOfFriendship: Int = 0,

        @get:Max(1)
        var status: Int = 1
)



