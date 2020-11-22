package io.openliberty.guides.cos.exception;

public class InvalidArgumentSizeException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentSizeException() {
        super();
    }

    public InvalidArgumentSizeException(String message) {
        super(message);
    }
}