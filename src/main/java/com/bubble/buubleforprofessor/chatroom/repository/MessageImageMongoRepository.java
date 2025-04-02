package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.doc.MessageImageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageMongoRepository extends MongoRepository<MessageImageMongo, String> {
}
