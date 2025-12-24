package com.bill.chess.domain.exception;

public class InvalidPieceException extends RuntimeException {

    public InvalidPieceException(String message) {
        super(message);
    }

}
