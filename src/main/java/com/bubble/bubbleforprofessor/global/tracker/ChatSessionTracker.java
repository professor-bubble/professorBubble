package com.bubble.buubleforprofessor.global.tracker;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionTracker {
    //교수가 여러 브라우저를 통해 접속할 수 있음을 가정하는것．서버가 여러개가 되면 ｒｅｄｉｓ와 같은 저장소를 사용해야함．
    private final Map<Long, Set<String>> roomProfessorSessions = new ConcurrentHashMap<>();

    public void addSession(Long roomId, String sessionId) {
        roomProfessorSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
    }

    public boolean removeSession(String sessionId) {
        roomProfessorSessions.values().forEach(set -> set.remove(sessionId));
        roomProfessorSessions.entrySet().removeIf(e -> e.getValue().isEmpty());
        return true;
    }

    public boolean isProfessorOnline(Long roomId) {
        return roomProfessorSessions.containsKey(roomId) && !roomProfessorSessions.get(roomId).isEmpty();
    }
}
