package com.bill.chess.domain.model;

import com.bill.chess.domain.enums.Color;
import java.util.List;
import java.util.Optional;

public record Board(Piece[][] squares, List<Move> history, Position whiteKingPos, Position blackKingPos) {

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

    public Position kingPos(Color color) {
        return color.isWhite() ? whiteKingPos : blackKingPos;
    }
}
