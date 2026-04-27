package com.api.openAi.controller;

import com.api.openAi.entity.ChatRequest;
import com.api.openAi.entity.ChatResponse;
import com.api.openAi.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final Logger logger = Logger.getLogger("ChatController");

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        logger.info("Chat API called with payload: "+ request.getMessage());
        String reply = chatService.getChatResponse(request.getMessage());

        logger.info("Received response for payload: "+ request.getMessage() + ", response: "+ reply);
        return new ChatResponse(reply);
    }
}