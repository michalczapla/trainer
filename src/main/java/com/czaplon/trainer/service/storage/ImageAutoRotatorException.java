package com.czaplon.trainer.service.storage;

public class ImageAutoRotatorException extends RuntimeException {
    public ImageAutoRotatorException(String message) {
        super(message);
    }

    public ImageAutoRotatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
