package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageMongo,String> {
    List<MessageMongo> findBySendTimeBetween(LocalDateTime sendTime, LocalDateTime now);
    List<MessageMongo> findMessagesByChatroomId(int chatroomId);
}
