package com.app.blender_helper.controller;


import com.app.blender_helper.entity.ChatRequest;
import com.app.blender_helper.entity.ChatResponse;
import com.app.blender_helper.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final Logger logger = Logger.getLogger("ChatController");

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        logger.info("Chat API called with payload: "+ request.getInput());
        String reply = chatService.getChatResponse(request.getInput());

        logger.info("Received response for payload: "+ request.getInput() + ", response: "+ reply);
        return new ChatResponse(reply);
    }
}
