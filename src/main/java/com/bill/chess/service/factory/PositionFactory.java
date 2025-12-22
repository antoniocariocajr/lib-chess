package com.bill.chess.service.factory;

import com.bill.chess.domain.model.Position;

import static com.bill.chess.service.validation.PositionValidation.validateNotation;
import static com.bill.chess.service.validation.PositionValidation.validatePosition;

public final class PositionFactory {

    public static Position fromNotation(String notation) {
        validateNotation(notation);

        char fileChar = notation.charAt(0);
        char rankChar = notation.charAt(1);

        int file = fileChar - 'a';
        int rank = Character.getNumericValue(rankChar);

        return of(rank, file);
    }

    public static Position fromFen(String fen) {

        if (fen.equals("-"))
            return null;

        validateNotation(fen);

        char fileChar = fen.charAt(0);
        char rankChar = fen.charAt(1);

        int file = fileChar - 'a';
        int rank = Character.getNumericValue(rankChar);

        return of(rank, file);
    }

    public static Position of(int rank, int file) {
        validatePosition(rank, file);
        return new Position(rank, file);
    }

    public static String toNotation(Position position) {
        validatePosition(position);
        char fileChar = (char) ('a' + position.file());
        return "" + fileChar + position.rank();
    }
}
