package com.bill.chess.domain.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

import static com.bill.chess.domain.rule.AttackDetector.isSquareAttacked;
import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;

public final class CastlingGenerator {

    public List<Move> generate(Board board, Color color, Set<CastleRight> rights) {
        List<Move> moves = new ArrayList<>(2);
        boolean ks = color.isWhite() ? rights.contains(CastleRight.WHITE_KINGSIDE)
                : rights.contains(CastleRight.BLACK_KINGSIDE);
        boolean qs = color.isWhite() ? rights.contains(CastleRight.WHITE_QUEENSIDE)
                : rights.contains(CastleRight.BLACK_QUEENSIDE);
        int rank = color.isWhite() ? 1 : 8;
        Position kingPos = PositionFactory.of(rank, 4);
        Piece king = board.pieceAt(kingPos)
                .filter(Piece::isKing)
                .orElse(null);
        if (king == null || isInCheck(board, color))
            return moves;

        // Kingside
        if (ks
                && board.pieceAt(PositionFactory.of(rank, 5)).isEmpty()
                && board.pieceAt(PositionFactory.of(rank, 6)).isEmpty()
                && !isSquareAttacked(board, PositionFactory.of(rank, 5), color.opposite())
                && !isSquareAttacked(board, PositionFactory.of(rank, 6), color.opposite())) {
            moves.add(MoveFactory.castle(kingPos, PositionFactory.of(rank, 6), king));
        }
        // Queenside
        if (qs
                && board.pieceAt(PositionFactory.of(rank, 3)).isEmpty()
                && board.pieceAt(PositionFactory.of(rank, 2)).isEmpty()
                && board.pieceAt(PositionFactory.of(rank, 1)).isEmpty()
                && !isSquareAttacked(board, PositionFactory.of(rank, 3), color.opposite())
                && !isSquareAttacked(board, PositionFactory.of(rank, 2), color.opposite())) {
            moves.add(MoveFactory.castle(kingPos, PositionFactory.of(rank, 2), king));
        }
        return moves;
    }
}
