package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ProfessorRepository professorRepository;
    @Override
    public void createChatroom(Professor professor) {
        if(chatroomRepository.existsChatroomByProfessor(professor))
        {
            throw new CustomException(ErrorCode.EXISTENT_CHATROOM);
        }
        Chatroom chatroom = new Chatroom(professor);
        chatroomRepository.save(chatroom);
    }

}
