package com.app.blender_helper.entity;

import lombok.Getter;


public class ChatResponse {

    private String reply;

    public ChatResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }
}
