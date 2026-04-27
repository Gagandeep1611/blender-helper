package com.app.blender_helper.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChatRequest {

    // Reject empty requests and bound prompt size at the API edge.
    @NotBlank(message = "message is required")
    @Size(max = 4000, message = "message must be at most 4000 characters")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
