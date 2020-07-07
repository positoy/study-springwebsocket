package com.example.demo;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository repository;

    public ChatHandler(ObjectMapper objectMapper, ChatRoomRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished");
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("handleTextMessage");

        String payload = message.getPayload();
        System.out.println(payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom chatRoom = repository.getChatRoom(chatMessage.getChatRoomId());

        if (chatMessage.getType().equals("JOIN")) {
            chatRoom.join(session);
            chatMessage.setMessage(chatMessage.getWriter() + " has joined the room.");
            chatMessage.setWriter("Admin");
            chatRoom.send(chatMessage, objectMapper);
        } else if (chatMessage.getType().equals("CHAT")) {
            chatRoom.send(chatMessage, objectMapper);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed");
        repository.remove(session);
    }
}
