package com.bill.chess.domain.model;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;

import jakarta.validation.constraints.NotNull;

public record Piece(@NotNull PieceType type, @NotNull Color color) {
    public boolean isWhite() {
        return color == Color.WHITE;
    }

    public boolean isPawn() {
        return type == PieceType.PAWN;
    }

    public boolean isKing() {
        return type == PieceType.KING;
    }

    public boolean isQueen() {
        return type == PieceType.QUEEN;
    }

    public boolean isBishop() {
        return type == PieceType.BISHOP;
    }

    public boolean isKnight() {
        return type == PieceType.KNIGHT;
    }

    public boolean isRook() {
        return type == PieceType.ROOK;
    }

    public char symbol() {
        return type.symbol();
    }

}
