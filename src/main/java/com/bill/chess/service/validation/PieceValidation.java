package com.bill.chess.service.validation;

import com.bill.chess.domain.model.Piece;
import com.bill.chess.infra.exception.InvalidPieceException;

public final class PieceValidation {

    public static void validatePiece(Piece piece) {
        if (piece.isNull()) {
            throw new InvalidPieceException("Piece cannot be null");
        }
    }

    public static void validateUnicode(String unicode) {
        if (unicode == null || unicode.length() != 1) {
            throw new InvalidPieceException("Unicode must be a single character");
        }
    }

    public static void validateUnicode(char unicode) {
        if (Character.isWhitespace(unicode)) {
            throw new InvalidPieceException("Unicode cannot be a whitespace");
        }
        switch (Character.toUpperCase(unicode)) {
            case 'P':
            case 'R':
            case 'N':
            case 'B':
            case 'Q':
            case 'K':
                break;
            default:
                throw new InvalidPieceException("Invalid unicode: " + unicode);
        }
    }

}
