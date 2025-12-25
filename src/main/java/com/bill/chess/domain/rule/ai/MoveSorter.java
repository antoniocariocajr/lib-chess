package com.bill.chess.domain.rule.ai;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class MoveSorter {

    private static final Map<PieceType, Integer> PIECE_VALUES = Map.of(
            PieceType.PAWN, 100,
            PieceType.KNIGHT, 320,
            PieceType.BISHOP, 330,
            PieceType.ROOK, 500,
            PieceType.QUEEN, 900,
            PieceType.KING, 20000);

    private MoveSorter() {
    }

    public static List<Move> sortMoves(Board board, List<Move> moves) {
        moves.sort(Comparator.comparingInt((Move m) -> getMoveScore(board, m)).reversed());
        return moves;
    }

    private static int getMoveScore(Board board, Move move) {
        int score = 0;

        // 1. Captures (MVV-LVA)
        if (move.captured().isPresent()) {
            Piece victim = move.captured().get();
            Piece aggressor = move.pieceMoved();
            score = 10 * PIECE_VALUES.get(victim.type()) - PIECE_VALUES.get(aggressor.type()) + 1000;
        }

        // 2. Promotions
        if (move.promotion().isPresent()) {
            score += PIECE_VALUES.get(move.promotion().get().type()) + 500;
        }

        // 3. Castling
        if (move.isCastling()) {
            score += 100;
        }

        return score;
    }
}
