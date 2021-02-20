/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsService.kt
*/

package no.enterprise2.contexam.usercollections.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Repository
interface FriendshipRepository : CrudRepository<Friendship, String> {

}

@Service
@Transactional
class FriendshipService(
        private val friendshipRepository: FriendshipRepository
) {

    fun findByEager(friendshipId: String): Friendship? {

        val friendship = friendshipRepository.findById(friendshipId).orElse(null)

        return friendship
    }

    fun createFriendship(friendshipId: String, userId: String, friendId: String, user1: User, user2: User, status: Int): Boolean {

        if (friendshipRepository.existsById(friendshipId)) {
            return false
        }

        if (status == 1) {

            val friendship = Friendship()
            friendship.friendId = friendId
            friendship.user1 = user1
            friendship.user2 = user2

            return true
        } else {
            return false
        }
    }
}