package com.bill.chess.domain.engine;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

import java.util.Map;

public final class EvaluationFunction {

    private EvaluationFunction() {
    }

    private static final Map<PieceType, Integer> MATERIAL_VALUES = Map.of(
            PieceType.PAWN, 100,
            PieceType.KNIGHT, 320,
            PieceType.BISHOP, 330,
            PieceType.ROOK, 500,
            PieceType.QUEEN, 900,
            PieceType.KING, 20000);

    private static final int[][] PAWN_PST = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
            { 5, 5, 10, 25, 25, 10, 5, 5 },
            { 0, 0, 0, 20, 20, 0, 0, 0 },
            { 5, -5, -10, 0, 0, -10, -5, 5 },
            { 5, 10, 10, -20, -20, 10, 10, 5 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    private static final int[][] KNIGHT_PST = {
            { -50, -40, -30, -30, -30, -30, -40, -50 },
            { -40, -20, 0, 0, 0, 0, -20, -40 },
            { -30, 0, 10, 15, 15, 10, 0, -30 },
            { -30, 5, 15, 20, 20, 15, 5, -30 },
            { -30, 0, 15, 20, 20, 15, 0, -30 },
            { -30, 5, 10, 15, 15, 10, 5, -30 },
            { -40, -20, 0, 5, 5, 0, -20, -40 },
            { -50, -40, -30, -30, -30, -30, -40, -50 }
    };

    private static final int[][] BISHOP_PST = {
            { -20, -10, -10, -10, -10, -10, -10, -20 },
            { -10, 0, 0, 0, 0, 0, 0, -10 },
            { -10, 0, 5, 10, 10, 5, 0, -10 },
            { -10, 5, 5, 10, 10, 5, 5, -10 },
            { -10, 0, 10, 10, 10, 10, 0, -10 },
            { -10, 10, 10, 10, 10, 10, 10, -10 },
            { -10, 5, 0, 0, 0, 0, 5, -10 },
            { -20, -10, -10, -10, -10, -10, -10, -20 }
    };

    public static int evaluate(Board board) {
        int score = 0;

        for (int rank = 1; rank <= 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(rank, file);
                var pieceOp = board.pieceAt(pos);
                if (pieceOp.isPresent()) {
                    Piece piece = pieceOp.get();
                    int value = MATERIAL_VALUES.get(piece.type());
                    value += getPositionalValue(piece, rank - 1, file);

                    if (piece.isWhite()) {
                        score += value;
                    } else {
                        score -= value;
                    }
                }
            }
        }

        return score;
    }

    private static int getPositionalValue(Piece piece, int rank, int file) {
        int[][] pst = switch (piece.type()) {
            case PAWN -> PAWN_PST;
            case KNIGHT -> KNIGHT_PST;
            case BISHOP -> BISHOP_PST;
            default -> null;
        };

        if (pst == null)
            return 0;

        // If it's black, we reverse the line to look at it from their point of view.
        int accessRank = piece.isWhite() ? 7 - rank : rank;
        return pst[accessRank][file];
    }
}
