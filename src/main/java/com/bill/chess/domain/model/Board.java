package com.bill.chess.domain.model;

import java.util.List;
import java.util.Optional;

public record Board(Piece[][] squares, List<Move> history) {

    public Optional<Piece> pieceAt(Position position) {
        return Optional.ofNullable(squares[position.rank()][position.file()]);
    }
}
