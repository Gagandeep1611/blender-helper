package com.app.blender_helper.entity;

import lombok.Getter;

@Getter
public class ChatResponse {

    private String reply;

    public ChatResponse(String reply){
        this.reply = reply;
    }
}
