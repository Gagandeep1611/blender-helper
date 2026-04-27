package com.app.blender_helper.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    private final OpenAIClient client;

    public ChatService(@Value("${openai.api.key}") String apiKey) {

        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    public String getChatResponse(String input) {

        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                    .input(input)
                    .model("gpt-5.4-mini")
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
