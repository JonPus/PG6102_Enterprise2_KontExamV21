package no.enterprise2.contexam.usercollections.db

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun initTest() {
        userRepository.deleteAll()
    }

    @Test
    fun testCreateUser() {
        val id = "foo"
        assertTrue(userService.registerNewUser(id))
        assertTrue(userRepository.existsById(id))
    }

    @Test
    fun testFailCreateUserTwice() {
        val id = "foo"
        assertTrue(userService.registerNewUser(id))
        assertFalse(userService.registerNewUser(id))
    }

    @Test
    fun testAddFriendship() {

        val userId1 = "Lars"
        val userId2 = "Karl"

        userService.registerNewUser(userId1)
        userService.registerNewUser(userId2)

        val user1 = userService.findIdByEager(userId1)!!
        val user2 = userService.findIdByEager(userId2)!!

        userService.addFriendship(user1, user2, userId2, 1)
        userService.addFriendship(user2, user1, userId1, 1)

        assertTrue(user1.friendList.any { it.friendId == userId2 })
        assertTrue(user2.friendList.any { it.friendId == userId1 })
    }

    /*  @Test
      fun testDeleteFriendship() {

          val userId1 = "Lars"
          val userId2 = "Karl"

          userService.registerNewUser(userId1)
          userService.registerNewUser(userId2)

          val user1 = userService.findIdByEager(userId1)!!
          val user2 = userService.findIdByEager(userId2)!!

          userService.addFriendship(user1, user2, userId2, 1)
          userService.addFriendship(user2, user1, userId1, 1)

          assertTrue(user1.friendList.any { it.friendId == userId2 })
          assertTrue(user2.friendList.any { it.friendId == userId1 })

          userService.deleteFriendship(userId1, userId2)



          //val afterDelete = userService.findIdByEager(userId1)!!

          val afterDelete = userRepository.lockedFind(userId1)!!

          val friendship = afterDelete.friendList.find { it.friendId == userId2 }

          val test = afterDelete.friendList.size

          println(test)
      }*/
}