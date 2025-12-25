package com.bill.chess.domain.factory;

import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.validation.MoveValidation;
import com.bill.chess.domain.validation.PieceValidation;

public final class MoveFactory {
    private MoveFactory() {
    }

    public static Move of(Position from, Position to, Piece capturedPiece, Piece promoted, Piece pieceMoved,
            boolean isCastling, boolean isEnPassant) {
        MoveValidation.validatePositions(from, to);
        return new Move(from, to, capturedPiece, promoted, pieceMoved, isCastling, isEnPassant);
    }

    public static Move quiet(Position from, Position to, Piece pieceMoved) {
        PieceValidation.validatePiece(pieceMoved);
        return of(from, to, null, null, pieceMoved, false, false);
    }

    public static Move capture(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        MoveValidation.validateCapture(capturedPiece, pieceMoved);
        return of(from, to, capturedPiece, null, pieceMoved, false, false);
    }

    public static Move promotion(Position from, Position to, Piece promoted, Piece pieceMoved) {
        MoveValidation.validatePromotion(to, promoted, pieceMoved);
        return of(from, to, null, promoted, pieceMoved, false, false);
    }

    public static Move promotionAndCapture(Position from, Position to, Piece capturedPiece, Piece promoted,
            Piece pieceMoved) {
        MoveValidation.validatePromotion(to, promoted, pieceMoved);
        MoveValidation.validateCapture(capturedPiece, pieceMoved);
        return of(from, to, capturedPiece, promoted, pieceMoved, false, false);
    }

    public static Move enPassant(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        MoveValidation.validateEnPassant(from, to, capturedPiece, pieceMoved);
        return of(from, to, capturedPiece, null, pieceMoved, false, true);
    }

    public static Move castle(Position from, Position to, Piece pieceMoved) {
        MoveValidation.validateCastling(from, to, pieceMoved);
        return of(from, to, null, null, pieceMoved, true, false);
    }

}
