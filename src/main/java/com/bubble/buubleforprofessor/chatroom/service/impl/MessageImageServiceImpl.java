package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.entity.MessageImage;
import com.bubble.buubleforprofessor.chatroom.repository.MessageImageRepository;
import com.bubble.buubleforprofessor.chatroom.service.MessageImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageImageServiceImpl implements MessageImageService {
    private final MessageImageRepository messageImageRepository;
    @Override
    public void save(MessageImage image) {
        messageImageRepository.save(image);
    }
}
