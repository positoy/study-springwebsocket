package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ChatMessage {
    private String chatRoomId;
    private String writer;
    private String message;
    private String type;
}
