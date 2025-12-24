package com.bill.chess.domain.validation;

import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.exception.InvalidPositionException;

public final class PositionValidation {

    public static void validateNotation(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new InvalidPositionException("Invalid notation: " + notation);
        }
    }

    public static void validatePosition(Position position) {
        if (position == null) {
            throw new InvalidPositionException("Position cannot be null");
        }
        validatePosition(position.rank(), position.file());

    }

    public static void validatePosition(int rank, int file) {
        if (file < 0 || file > 7 || rank < 1 || rank > 8) {
            char fileChar = (char) ('a' + file);
            throw new InvalidPositionException("Invalid position: " + fileChar + rank);
        }
    }
}
