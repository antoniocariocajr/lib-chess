package com.bill.chess.domain.exception;

public class InvalidBoardException extends RuntimeException {

    public InvalidBoardException(String message) {
        super(message);
    }

}
