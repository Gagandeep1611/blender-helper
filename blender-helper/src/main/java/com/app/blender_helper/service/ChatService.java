package com.app.blender_helper.service;

import com.app.blender_helper.exceptionHandling.ChatServiceException;
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

        //. Keep OpenAI configuration in Spring so environments are consistent.
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
            //. Translate upstream failures into a stable application exception.
            logger.error("OpenAI request failed for model={}", model, e);
            throw new ChatServiceException("Failed to generate chat response", e);
        }
    }
}
