package com.bill.chess.domain.model;

import java.util.Objects;
import java.util.Optional;

import com.bill.chess.service.mapper.PieceMapper;
import com.bill.chess.service.mapper.PositionMapper;

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

    public String toUci() {
        StringBuilder sb = new StringBuilder();
        sb.append(PositionMapper.toNotation(from)).append(PositionMapper.toNotation(to));
        promotion().ifPresent(p -> sb.append(PieceMapper.toUnicode(p)));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Move move = (Move) o;
        return Objects.equals(PositionMapper.toNotation(to), PositionMapper.toNotation(move.to))
                && Objects.equals(PositionMapper.toNotation(from), PositionMapper.toNotation(move.from));
    }

    @Override
    public int hashCode() {
        return Objects.hash(PositionMapper.toNotation(from), PositionMapper.toNotation(to));
    }
}
