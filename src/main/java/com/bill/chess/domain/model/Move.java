package com.bill.chess.domain.model;

import java.util.Objects;
import java.util.Optional;

import com.bill.chess.service.factory.PositionFactory;

import jakarta.validation.constraints.NotNull;

public record Move(
        @NotNull Position from,
        @NotNull Position to,
        Piece capturedPiece,
        Piece pawnPromotion,
        Piece pieceMoved,
        boolean isCastling,
        boolean isEnPassant) {

    public Optional<Piece> captured() {
        return Optional.ofNullable(capturedPiece());
    }

    public Optional<Piece> promotion() {
        return Optional.ofNullable(pawnPromotion());
    }

    public Optional<Piece> moved() {
        return Optional.ofNullable(pieceMoved());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Move move = (Move) o;
        return Objects.equals(PositionFactory.toNotation(to), PositionFactory.toNotation(move.to))
                && Objects.equals(PositionFactory.toNotation(from), PositionFactory.toNotation(move.from));
    }

    @Override
    public int hashCode() {
        return Objects.hash(PositionFactory.toNotation(from), PositionFactory.toNotation(to));
    }
}
