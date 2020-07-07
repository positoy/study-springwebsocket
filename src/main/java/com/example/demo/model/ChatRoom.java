package com.example.demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoom {
    private String id;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom create(@NonNull String name) {
        ChatRoom created = new ChatRoom();
        created.name = name;
        created.id = UUID.randomUUID().toString();
        return created;
    }

    public void join(WebSocketSession session) {
        sessions.add(session);
    }

    public void leave(WebSocketSession session) {
        sessions.remove(session);
    }

    public <T> void send(T messageObject, ObjectMapper objectMapper) {
        TextMessage message = null;
        try {
            message = new TextMessage(objectMapper.writeValueAsString(messageObject));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        TextMessage finalMessage = message;
        sessions.parallelStream().forEach(session-> {
            try {
                session.sendMessage(finalMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
