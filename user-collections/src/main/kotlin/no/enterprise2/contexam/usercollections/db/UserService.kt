/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsService.kt
*/

package no.enterprise2.contexam.usercollections.db

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Repository
interface UserRepository : CrudRepository<User, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.userId = :id")
    fun lockedFind(@Param(value = "id") userId: String): User?
}

@Service
@Transactional
class UserService(
        private val userRepository: UserRepository
        //private val postService: PostService
) {

    fun findIdByEager(userId: String): User? {
        val user = userRepository.findById(userId).orElse(null)

        if (user != null) {
            user.friendList.size
        }
        return user
    }

    fun registerNewUser(userId: String): Boolean {

        if (userRepository.existsById(userId)) {
            return false
        }

        val user = User()
        user.userId = userId
        user.firstName = "Foo"
        user.lastName = "Bar"
        user.email = "FooBar@gmail.com"
        userRepository.save(user)

        return true
    }

    private fun validateUser(userId: String) {
        if (!userRepository.existsById(userId)) {
            throw IllegalArgumentException("User $userId does not exist")
        }
    }

    fun acceptFriendship(userId: String, friendId: String, status: Int) {
        validateUser(userId)

        val sender = userRepository.lockedFind(userId)
        val friend = userRepository.lockedFind(friendId)

        addFriendship(sender!!, friend!!, friendId, status)
    }

    fun addFriendship(user1: User, user2: User, friendId: String, status: Int) {

        if (status == 1) {

            user1.friendList.find { it.friendId == friendId }
                    ?.apply { numberOfFriendship++ }
                    ?: Friendship().apply {
                        this.friendId = friendId
                        this.user1 = user1
                        this.user2 = user2
                        this.numberOfFriendship = 1
                    }.also {
                        user1.friendList.add(it)
                    }.also {
                        user2.friendList.add(it)
                    }

        } else {
            throw IllegalArgumentException("User did not accept the friend request")
        }
    }

    fun deleteFriendship(userId: String, friendId: String): Boolean {
        validateUser(userId)

        val user = userRepository.lockedFind(userId)!!

        val friendship = user.friendList.find { it.friendId == friendId }

        println(friendship)

        if (friendship == null || friendship.numberOfFriendship == 0) {
            throw IllegalArgumentException("User $userId does not have friends $friendId. :(")
        }

        friendship.numberOfFriendship--

        return true
    }
}

