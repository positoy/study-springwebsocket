package com.example.demo;

import com.example.demo.model.ChatRoom;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;

    public ChatRoomRepository() {
        chatRoomMap = Collections.unmodifiableMap(
                Stream.of(ChatRoom.create("#1"), ChatRoom.create("#2"), ChatRoom.create("#3"))
                        .collect(Collectors.toMap(ChatRoom::getId, Function.identity())));
    }

    public ChatRoom getChatRoom(String id) {
        return chatRoomMap.get(id);
    }

    public Collection<ChatRoom> getChatRooms() {
        return chatRoomMap.values();
    }

    public void remove(WebSocketSession session) {
        for (ChatRoom chatroom : chatRoomMap.values()) {
            chatroom.getSessions().remove(session);
        }
    }
}
