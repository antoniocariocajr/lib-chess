package com.bill.chess.infra.exception;

public class InvalidBoardException extends RuntimeException {

    public InvalidBoardException(String message) {
        super(message);
    }

}
