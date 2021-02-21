/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-02/cards-dto/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/dto/Rarity.kt
*/

package no.enterprise2.contexam.messages.dto

enum class MessageClass {

    POST,

    MESSAGE,

    PRIVATE_MESSAGE,

    PUBLIC_MESSAGE
}