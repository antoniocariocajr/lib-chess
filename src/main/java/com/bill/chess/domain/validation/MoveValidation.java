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
        if (Math.abs(from.file() - to.file()) != 1)
            throw new InvalidMoveException("En passant capture must change file by 1");
        if (Math.abs(from.rank() - to.rank()) != 1)
            throw new InvalidMoveException("En passant capture must change rank by 1");
        if (!capturedPiece.isPawn())
            throw new InvalidMoveException("En passant can only capture a pawn");
        if (capturedPiece.color() == pieceMoved.color())
            throw new InvalidMoveException("En passant can only be made on the opposite color");
    }

    public static void validateCastling(Position from, Position to, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        if (!pieceMoved.isKing())
            throw new InvalidMoveException("Castling can only be made by a king");
        if (Math.abs(from.file() - to.file()) != 2)
            throw new InvalidMoveException("Castling must move the king 2 squares horizontally");
        if (from.rank() != to.rank())
            throw new InvalidMoveException("Castling must be on the same rank");

        int expectedRank = pieceMoved.isWhite() ? 1 : 8;
        if (from.rank() != expectedRank)
            throw new InvalidMoveException("Castling must be on the correct rank for the piece's color");
    }

    public static void validatePromotion(Position from, Position to, Piece promoted, Piece pieceMoved) {
        validatePositions(from, to);
        PieceValidation.validatePiece(pieceMoved);
        PieceValidation.validatePiece(promoted);
        if (!pieceMoved.isPawn())
            throw new InvalidMoveException("Promotion can only be made by a pawn");
        if (Math.abs(from.rank() - to.rank()) != 1)
            throw new InvalidMoveException("Promotion move must change rank by 1");
        if (!promoted.isQueen() && !promoted.isRook() && !promoted.isBishop() && !promoted.isKnight())
            throw new InvalidMoveException("Promotion can only be made to a queen, rook, bishop or knight");

        int promotionRank = pieceMoved.isWhite() ? 8 : 1;
        if (to.rank() != promotionRank)
            throw new InvalidMoveException("Pawn can only promote on the last rank");
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
