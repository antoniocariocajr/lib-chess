package com.bill.chess.domain.exception;

public class InvalidColorException extends RuntimeException {
    public InvalidColorException(String message) {
        super(message);
    }
}
