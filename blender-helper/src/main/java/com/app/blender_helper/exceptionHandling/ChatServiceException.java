package com.app.blender_helper.exceptionHandling;

//. Domain exception used to avoid leaking SDK-specific failures into controllers.
public class ChatServiceException extends RuntimeException {

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
