package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageMongo,String> {
    List<MessageMongo> findBySendTimeBetween(LocalDateTime sendTime, LocalDateTime now);
    List<MessageMongo> findMessagesByChatroomId(int chatroomId);

    @Query("{ 'chatroomId': ?0, 'readByUserIds': { $size: 0 } }")
    List<MessageMongo> findMessagesUnreadByAnyone(Long chatroomId);


}
