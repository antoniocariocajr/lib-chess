package com.bill.chess.domain.model;

import java.util.List;
import java.util.Optional;

public record Board(Piece[][] squares, List<Move> history) {

    public Optional<Piece> pieceAt(Position position) {
        return Optional.ofNullable(squares[position.rank()][position.file()]);
    }

    public Piece[][] copySquares() {
        Piece[][] copy = new Piece[9][8];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(squares[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
}
