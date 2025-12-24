package com.bill.chess.domain.exception;

public class InvalidUciException extends RuntimeException {
    public InvalidUciException(String message) {
        super(message);
    }
}
