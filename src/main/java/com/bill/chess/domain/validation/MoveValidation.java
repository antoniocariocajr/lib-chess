package com.bill.chess.domain.validation;

import com.bill.chess.domain.exception.InvalidPositionException;
import com.bill.chess.domain.exception.InvalidUciException;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.exception.InvalidMoveException;

public final class MoveValidation {

    public static void validatePositions(Position from, Position to) {

        if (from == null || to == null)
            throw new InvalidPositionException("Move cannot be null");

        if (from.equals(to))
            throw new InvalidMoveException("Move cannot be from and to the same position");
    }

    public static void validateEnPassant(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        PieceValidation.validatePiece(capturedPiece);
        if (!pieceMoved.isPawn())
            throw new InvalidMoveException("En passant can only be made by a pawn");
        if (from.file() != to.file())
            throw new InvalidMoveException("En passant can only be made by a pawn on the same file");
        if (from.rank() != to.rank() + 2)
            throw new InvalidMoveException("En passant can only be made by a pawn on the same rank");
        if (!capturedPiece.isPawn())
            throw new InvalidMoveException("En passant can only be made by a pawn");
        if (capturedPiece.color() == pieceMoved.color())
            throw new InvalidMoveException("En passant can only be made by a pawn of the opposite color");
    }

    public static void validateCastling(Position from, Position to, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        if (!pieceMoved.isKing())
            throw new InvalidMoveException("Castling can only be made by a king");
        if (from.file() != to.file() + 2)
            throw new InvalidMoveException("Castling can only be made by a king on the same file");
        if (from.rank() != to.rank())
            throw new InvalidMoveException("Castling can only be made by a king on the same rank");
        if (from.rank() != 1 && from.rank() != 8)
            throw new InvalidMoveException("Castling can only be made by a king on the first or eighth rank");
        if (from.rank() == 1 && pieceMoved.isWhite())
            throw new InvalidMoveException("Castling can only be made by a white king on the first rank");
        if (from.rank() == 8 && !pieceMoved.isWhite())
            throw new InvalidMoveException("Castling can only be made by a black king on the eighth rank");
    }

    public static void validatePromotion(Position from, Position to, Piece promoted, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        PieceValidation.validatePiece(promoted);
        if (!pieceMoved.isPawn())
            throw new InvalidMoveException("Promotion can only be made by a pawn");
        if (from.rank() != to.rank() + 1)
            throw new InvalidMoveException("Promotion can only be made by a pawn on the same rank");
        if (!promoted.isQueen() && !promoted.isRook() && !promoted.isBishop() && !promoted.isKnight())
            throw new InvalidMoveException("Promotion can only be made to a queen, rook, bishop or knight");
        if (to.rank() == 8 && pieceMoved.isWhite())
            throw new InvalidMoveException("Promotion Invalid in this position");
        if (to.rank() == 1 && !pieceMoved.isWhite())
            throw new InvalidMoveException("Promotion Invalid in this position");
    }

    public static void validateCapture(Position from, Position to, Piece capturedPiece, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        PieceValidation.validatePiece(capturedPiece);
        if (capturedPiece.color() == pieceMoved.color())
            throw new InvalidMoveException("Move cannot capture a piece of the same color");
    }

    public static void validateUci(String uci) {
        if (uci == null || uci.length() < 4 || uci.length() > 5)
            throw new InvalidUciException("Invalid uci: " + uci);
    }
}
