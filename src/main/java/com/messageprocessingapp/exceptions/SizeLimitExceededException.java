package com.messageprocessingapp.exceptions;

public class SizeLimitExceededException extends Exception {
    public SizeLimitExceededException() {

    }
    public SizeLimitExceededException(String message) {
        super(message);
    }
}
