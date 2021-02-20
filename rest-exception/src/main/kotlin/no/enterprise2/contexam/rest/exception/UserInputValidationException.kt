package no.enterprise2.contexam.rest.exception

class UserInputValidationException(
        message: String,
        val httpCode: Int = 400
) : RuntimeException(message)