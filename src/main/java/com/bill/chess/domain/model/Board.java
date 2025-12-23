package com.bill.chess.domain.model;

import java.util.List;
import java.util.Optional;

public record Board(Piece[][] squares, List<Move> history) {

    public Optional<Piece> pieceAt(Position position) {
        return Optional.ofNullable(squares[position.rank()][position.file()]);
    }

    public void doMove(Move move) {
        Position to = move.to();
        Position from = move.from();
        if (squares[from.rank()][from.file()].isPawn() && move.promotion().isPresent()) {
            squares[to.rank()][to.file()] = move.pawnPromotion();
        } else {
            squares[to.rank()][to.file()] = squares[from.rank()][from.file()];
        }
        squares[from.rank()][from.file()] = null;
        this.history.addLast(move);
    }
}
