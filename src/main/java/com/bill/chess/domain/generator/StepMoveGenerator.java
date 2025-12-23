package com.bill.chess.domain.generator;

import java.util.*;

import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.rule.ValidPositionRule;

public final class StepMoveGenerator implements MoveGenerator {

    public static final int[][] KNIGHT_DELTAS = { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 },
            { 2, -1 }, { 2, 1 } };
    public static final int[][] KING_DELTAS = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 },
            { 1, 0 }, { 1, 1 } };

    public static final StepMoveGenerator KNIGHT = new StepMoveGenerator(KNIGHT_DELTAS);
    public static final StepMoveGenerator KING = new StepMoveGenerator(KING_DELTAS);

    private final int[][] deltas;

    public StepMoveGenerator(int[][] deltas) {
        this.deltas = deltas;
    }

    @Override
    public List<Move> generate(Board board, Position from, Piece piece, Position enPassant) {
        List<Move> moves = new ArrayList<>(deltas.length);
        for (int[] delta : deltas) {
            int rank = from.rank() + delta[0], file = from.file() + delta[1];
            if (!ValidPositionRule.isValid(rank, file))
                continue;
            Position to = PositionFactory.of(rank, file);
            Optional<Piece> occupationSquare = board.pieceAt(to);
            if (occupationSquare.isEmpty()) {
                moves.add(MoveFactory.quiet(from, to, piece));
            } else if (occupationSquare.get().color() != piece.color()) {
                moves.add(MoveFactory.capture(from, to, occupationSquare.get(), piece));
            }
        }
        return moves;
    }
}
