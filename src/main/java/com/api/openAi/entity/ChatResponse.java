package com.api.openAi.entity;

import lombok.Getter;

@Getter
public class ChatResponse {

    private String reply;

    public ChatResponse(String reply){
        this.reply = reply;
    }
}
