package com.bill.chess.infra.exception;

public class InvalidPieceException extends RuntimeException {

    public InvalidPieceException(String message) {
        super(message);
    }

}
