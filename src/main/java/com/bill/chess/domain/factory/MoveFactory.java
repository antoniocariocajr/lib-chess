package com.bill.chess.domain.factory;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.validation.MoveValidation;

public final class MoveFactory {

    public static Move quiet(Position from, Position to, Piece pieceMoved) {
        MoveValidation.validatePositions(from, to);
        return new Move(from, to, null, null, pieceMoved, false, false);
    }

    public static Move capture(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        MoveValidation.validateCapture(from, to, capturedPiece, pieceMoved);
        return new Move(from, to, capturedPiece, null, pieceMoved, false, false);
    }

    public static Move promotion(Position from, Position to, Piece promoted, Piece pieceMoved) {
        MoveValidation.validatePromotion(from, to, promoted, pieceMoved);
        return new Move(from, to, null, promoted, pieceMoved, false, false);
    }

    public static Move promotionAndCapture(Position from, Position to, Piece capturedPiece, Piece promoted,
            Piece pieceMoved) {
        MoveValidation.validatePromotion(from, to, promoted, pieceMoved);
        MoveValidation.validateCapture(from, to, capturedPiece, pieceMoved);
        return new Move(from, to, capturedPiece, promoted, pieceMoved, false, false);
    }

    public static Move enPassant(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        MoveValidation.validateEnPassant(from, to, capturedPiece, pieceMoved);
        return new Move(from, to, capturedPiece, null, pieceMoved, false, true);
    }

    public static Move castle(Position from, Position to, Piece pieceMoved) {
        MoveValidation.validateCastling(from, to, pieceMoved);
        return new Move(from, to, null, null, pieceMoved, true, false);
    }

    public static Move fromUci(String uci) {
        MoveValidation.validateUci(uci);
        Position from = PositionFactory.fromNotation(uci.substring(0, 2));
        Position to = PositionFactory.fromNotation(uci.substring(2, 4));
        if (uci.length() == 5) {
            Piece promoted = PieceFactory.toPiece(uci.substring(4));
            Piece moved = new Piece(PieceType.PAWN, promoted.color());
            return promotion(from, to, promoted, moved);
        }
        return quiet(from, to, null);
    }

    public static Move fromUci(String uci, Piece pieceMoved) {
        MoveValidation.validateUci(uci);
        Position from = PositionFactory.fromNotation(uci.substring(0, 2));
        Position to = PositionFactory.fromNotation(uci.substring(2, 4));
        if (uci.length() == 5) {
            Piece promoted = PieceFactory.toPiece(uci.substring(4));
            Piece moved = new Piece(PieceType.PAWN, pieceMoved.color());
            return promotion(from, to, promoted, moved);
        }
        return quiet(from, to, pieceMoved);
    }

    public static String toUci(Move move) {
        StringBuilder sb = new StringBuilder();
        sb.append(PositionFactory.toNotation(move.from())).append(PositionFactory.toNotation(move.to()));
        move.promotion().ifPresent(p -> sb.append(PieceFactory.toUnicode(p)));
        return sb.toString();
    }
}
