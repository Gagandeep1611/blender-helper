package com.app.blender_helper.controller;


import com.app.blender_helper.model.ChatRequest;
import com.app.blender_helper.model.ChatResponse;
import com.app.blender_helper.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        //. Avoid logging raw user content; keep only request metadata.
        logger.info("Chat API called with messageLength={}", request.getMessage().length());
        String reply = chatService.getChatResponse(request.getMessage());

        //. Log response size for observability without persisting the reply.
        logger.info("Chat API completed with replyLength={}", reply.length());
        return new ChatResponse(reply);
    }
}
