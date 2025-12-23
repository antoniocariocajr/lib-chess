package com.bill.chess.domain.factory;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.infra.validation.PieceValidation;

public final class PieceFactory {

    public static Piece toPiece(char unicode) {

        PieceValidation.validateUnicode(unicode);

        PieceType pieceType = PieceType.fromSymbol(unicode);

        Color colorUnicode = Character.isUpperCase(unicode) ? Color.WHITE : Color.BLACK;

        return new Piece(pieceType, colorUnicode);
    }

    public static Piece toPiece(String unicode) {

        PieceValidation.validateUnicode(unicode);

        return toPiece(unicode.charAt(0));
    }

    public static char toUnicode(Piece piece) {
        PieceValidation.validatePiece(piece);
        return piece.isWhite() ? Character.toUpperCase(piece.type().symbol())
                : Character.toLowerCase(piece.type().symbol());
    }
}
