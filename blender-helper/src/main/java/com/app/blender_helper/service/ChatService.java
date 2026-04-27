package com.app.blender_helper.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final OpenAIClient client;
    private final String model;

    public ChatService(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.api.model}") String model
    ) {

        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
        this.model = model;
    }

    public String getChatResponse(String input) {

        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                    .input(input)
                    .model(model)
                    .build();

            Response response = client.responses().create(params);
            return response.output()
                    .stream()
                    .findFirst()
                    .flatMap(ResponseOutputItem::message)
                    .flatMap(message -> message.content().stream().findFirst())
                    .flatMap(ResponseOutputMessage.Content::outputText)
                    .map(ResponseOutputText::text)
                    .orElse("No response");
        } catch (Exception e) {
            logger.error("OpenAI request failed for model={}", model, e);
            throw new RuntimeException("Failed to generate chat response", e);
        }
    }
}
