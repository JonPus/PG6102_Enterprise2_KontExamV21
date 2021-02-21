package no.enterprise2.contexam.messages.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Repository
interface MessageRepository : CrudRepository<Message, String>

@Service
@Transactional
class MessageService(
        val messageRepository: MessageRepository,
        val em: EntityManager
) {

    fun createNewMessage(messageId: String, message: String, userId: String, friendId: String): Boolean {

        if (messageRepository.existsById(messageId)) {
            return false
        }

        val newMessage = Message(messageId, message, userId, friendId)

        messageRepository.save(newMessage)
        return true
    }

    fun deleteMessage(messageId: String) {
        messageRepository.deleteById(messageId)
    }

    fun getNextTimeline(size: Int, keySetId: String? = null, keySetMessage: Int? = null): List<Message> {

        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value $size")
        }

        if ((keySetId == null && keySetMessage != null) || (keySetId != null && keySetMessage == null)) {
            throw IllegalArgumentException("keySetId and keySetMessage should both be missing, or both present")
        }

        val query: TypedQuery<Message>
        if (keySetId == null) {

            query = em.createQuery(
                    "SELECT m FROM Message m ORDER BY m.friendId DESC, m.userId DESC",
                    Message::class.java
            )
        } else {
            query = em.createQuery(
                    "SELECT m FROM Message m WHERE m.friendId <? 2 OR (m.friendId =? 2 AND m.messageId <? 1) ORDER BY m.friendId DESC, m.messageId DESC",
                    Message::class.java
            )
            query.setParameter(1, keySetId)
            query.setParameter(2, keySetMessage)
        }
        query.maxResults = size

        return query.resultList

    }

}