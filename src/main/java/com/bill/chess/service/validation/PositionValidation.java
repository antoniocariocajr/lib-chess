package com.bill.chess.service.validation;

import com.bill.chess.domain.model.Position;
import com.bill.chess.infra.exception.InvalidPositionException;

public final class PositionValidation {

    public static void validateNotation(String notation){
        if (notation == null || notation.length() != 2) {
            throw new InvalidPositionException("Invalid notation: " + notation);
        }
    }
    public static void validatePosition(Position position){
        validatePosition(position.rank(), position.file());

    }
    public static void validatePosition(int rank, int file){
        if (file < 0 || file > 7 || rank < 1 || rank > 8) {
            char fileChar = (char) ('a' + file);
            throw new InvalidPositionException("Invalid position: " + String.valueOf(fileChar) + rank);
        }
    }
}
