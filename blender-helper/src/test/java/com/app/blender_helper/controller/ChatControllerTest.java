package com.app.blender_helper.controller;

import com.app.blender_helper.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@Import(ChatControllerTest.TestConfig.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatService chatService;

    @Test
    void chatReturnsReply() throws Exception {
        //. Verify the happy path without calling the real OpenAI client.
        when(chatService.getChatResponse("hello")).thenReturn("world");

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":"hello"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reply").value("world"));
    }

    @Test
    void chatRejectsBlankMessage() throws Exception {
        //. Reset the shared mock so this assertion is isolated from the happy-path test.
        reset(chatService);

        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"message":" "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("message: message is required"));

        verifyNoInteractions(chatService);
    }

    static class TestConfig {

        //. Provide a Mockito-backed ChatService bean for the MVC slice test.
        @Bean
        @Primary
        ChatService chatService() {
            return mock(ChatService.class);
        }
    }
}
