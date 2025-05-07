package com.bubble.bubbleforprofessor.global.listener;

import com.bubble.bubbleforprofessor.chatroom.service.MessageService;
import com.bubble.bubbleforprofessor.global.tracker.ChatSessionTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatSessionTracker tracker;
    private final MessageService messageService;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination(); // ex) /sub/chatroom/1/professor
        String sessionId = accessor.getSessionId();

        if (destination != null && destination.contains("/chatroom/") && destination.endsWith("/professor")) {
            // 채팅방 ID 추출
            String[] parts = destination.split("/");
            Long chatRoomId = Long.parseLong(parts[3]);

            // 여기서 교수 ID는 인증 정보에서 가져와야 함 (예: SecurityContext 또는 세션 기반)
            UUID professorId = (UUID) Objects.requireNonNull(accessor.getSessionAttributes()).get("userId");
            // 세션 트래킹
            tracker.addSession(chatRoomId, sessionId);

            // ❗ 교수 입장 시점에서 안 읽은 메시지 일괄 처리
            messageService.markMessagesAsReadByProfessor(chatRoomId, professorId);

        }
    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        tracker.removeSession(sessionId);
    }
}
