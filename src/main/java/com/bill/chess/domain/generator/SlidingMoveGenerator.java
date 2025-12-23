package com.bill.chess.domain.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.rule.ValidPositionRule;

public final class SlidingMoveGenerator implements MoveGenerator {

    private static final int[][] BISHOP_DELTAS = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
    private static final int[][] ROOK_DELTAS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private static final int[][] QUEEN_DELTAS = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 },
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public static final SlidingMoveGenerator BISHOP = new SlidingMoveGenerator(BISHOP_DELTAS);
    public static final SlidingMoveGenerator ROOK = new SlidingMoveGenerator(ROOK_DELTAS);
    public static final SlidingMoveGenerator QUEEN = new SlidingMoveGenerator(QUEEN_DELTAS);

    private final int[][] deltas;

    public SlidingMoveGenerator(int[][] deltas) {
        this.deltas = deltas;
    }

    @Override
    public List<Move> generate(Board board, Position from, Piece piece, Position enPassant) {
        List<Move> moves = new ArrayList<>(28);
        for (int[] delta : deltas) {
            int rank = from.rank(), file = from.file();
            while (true) {
                rank += delta[0];
                file += delta[1];
                if (!ValidPositionRule.isValid(rank, file))
                    break;
                Position to = PositionFactory.of(rank, file);
                Optional<Piece> occupationSquare = board.pieceAt(to);
                if (occupationSquare.isEmpty()) {
                    moves.add(MoveFactory.quiet(from, to, piece));
                } else {
                    if (occupationSquare.get().color() != piece.color())
                        moves.add(MoveFactory.capture(from, to, occupationSquare.get(), piece));
                    break;
                }
            }
        }
        return moves;
    }
}
