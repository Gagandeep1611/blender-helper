package com.app.blender_helper.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
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
    private final String prompt;

    public ChatService(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.api.model}") String model,
            @Value("${openai.api.prompt}") String prompt
    ) {

        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
        this.model = model;
        this.prompt = prompt;
    }

    public String getChatResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(model)
                    .addSystemMessage(prompt)// Set the model
                    .addUserMessage(input) // Add user input
                    .build();



            ChatCompletion completion = client.chat().completions().create(params);
            String response = String.valueOf(completion.choices().getFirst().message().content());

            return completion.choices().getFirst().message().content().orElseThrow();
        } catch (Exception e) {
            logger.error("OpenAI request failed for model={}", model, e);
            throw new RuntimeException("Failed to generate chat response", e);
        }
    }


}
