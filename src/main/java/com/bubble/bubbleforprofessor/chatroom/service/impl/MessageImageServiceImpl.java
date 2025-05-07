package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.service.MessageImageService;
import com.bubble.bubbleforprofessor.chatroom.entity.MessageImage;
import com.bubble.bubbleforprofessor.chatroom.repository.MessageImageRepository;
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
