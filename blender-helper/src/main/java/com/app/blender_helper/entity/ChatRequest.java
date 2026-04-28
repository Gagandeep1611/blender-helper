package com.app.blender_helper.entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @Size(min = 1, max = 200, message = "Message must be between 1 and 200 characters")
    private String input;
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
