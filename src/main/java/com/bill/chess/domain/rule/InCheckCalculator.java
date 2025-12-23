package com.bill.chess.domain.rule;

import java.util.Optional;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

public final class InCheckCalculator {
    private static final AttackDetector detector = new AttackDetector();

    public boolean isInCheck(Board board, Color side) {
        Position kingPos = findKing(board, side);
        return kingPos != null && detector.isSquareAttacked(board, kingPos, side.opposite());
    }

    public boolean isSquareAttacked(Board board, Position square, Color byColor) {
        return detector.isSquareAttacked(board, square, byColor);
    }

    private Position findKing(Board board, Color color) {
        for (int rank = 1; rank < 9; rank++)
            for (int file = 0; file < 8; file++) {
                Position position = PositionFactory.of(rank, file);
                Optional<Piece> piece = board.pieceAt(position);
                if (piece.isPresent() && piece.get().isKing() && piece.get().color() == color)
                    return position;
            }
        return null;
    }
}
