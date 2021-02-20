/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/RestAPI.kt
*/

package no.enterprise2.contexam.usercollections

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.enterprise2.contexam.rest.dto.RestResponseFactory
import no.enterprise2.contexam.rest.dto.WrappedResponse
import no.enterprise2.contexam.usercollections.db.UserService
import no.enterprise2.contexam.usercollections.dto.Command
import no.enterprise2.contexam.usercollections.dto.PatchResultDto
import no.enterprise2.contexam.usercollections.dto.PatchUserDto
import no.enterprise2.contexam.usercollections.dto.UserDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@Api(value = "/api/user-collections", description = "Operations on friendships between users on the social site")
@RequestMapping(
        path = ["/api/user-collections"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)

@RestController
class RestAPI(
        private val userService: UserService
) {
    @ApiOperation("Retrieve friendship collection information for a specific user")
    @GetMapping(path = ["/{userId}"])
    fun getUserInfo(
            @PathVariable("userId") userId: String
    ): ResponseEntity<WrappedResponse<UserDto>> {

        val user = userService.findIdByEager(userId)

        if (user == null) {
            return RestResponseFactory.notFound("User $userId not found")
        }
        return RestResponseFactory.payload(200, DtoConverter.transform(user))
    }

    @ApiOperation("Create a new user, with given userId")
    @PutMapping(path = ["/{userId}"])
    fun createUser(
            @PathVariable("userId") userId: String
    ): ResponseEntity<WrappedResponse<Void>> {
        val ok = userService.registerNewUser(userId)
        return if (!ok) RestResponseFactory.userFailure("User $userId already exists.")
        else RestResponseFactory.noPayload(201)
    }

    @ApiOperation("Execute a command on a user's collection, like for example sending a friend request")
    @PatchMapping(
            path = ["/{userId}"],
            consumes = [(MediaType.APPLICATION_JSON_VALUE)]
    )
    fun patchUser(
            @PathVariable("userId") userId: String,
            @RequestBody dto: PatchUserDto
    ): ResponseEntity<WrappedResponse<PatchResultDto>> {

        if (dto.command == null) {
            return RestResponseFactory.userFailure("Missing command.")
        }

        val friendId = dto.friendId
                ?: return RestResponseFactory.userFailure("Missing friendId")
        val user1 = userService.findIdByEager(userId)
        val user2 = userService.findIdByEager(friendId)

        if (dto.command == Command.ADD_FRIENDSHIP) {
            try {
                userService.addFriendship(user1!!, user2!!, friendId, 1)
            } catch (e: IllegalArgumentException) {
                return RestResponseFactory.userFailure(e.message ?: "Failed to return friendship $friendId")
            }
            return RestResponseFactory.payload(200, PatchResultDto())
        }
        return RestResponseFactory.userFailure("Unrecognized command: ${dto.command}")
    }
}