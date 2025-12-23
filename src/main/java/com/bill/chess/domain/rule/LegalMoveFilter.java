package com.bill.chess.domain.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.generator.CastlingGenerator;
import com.bill.chess.domain.generator.MoveGenerator;
import com.bill.chess.domain.generator.PawnMoveGenerator;
import com.bill.chess.domain.generator.StepMoveGenerator;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.generator.SlidingMoveGenerator;

public final class LegalMoveFilter {

    private static final InCheckCalculator checkCalc = new InCheckCalculator();
    private static final CastlingGenerator castlingGen = new CastlingGenerator();
    private static final Map<PieceType, MoveGenerator> generators = Map.of(
            PieceType.PAWN, new PawnMoveGenerator(),
            PieceType.KNIGHT, StepMoveGenerator.KNIGHT,
            PieceType.KING, StepMoveGenerator.KING,
            PieceType.BISHOP, SlidingMoveGenerator.BISHOP,
            PieceType.ROOK, SlidingMoveGenerator.ROOK,
            PieceType.QUEEN, SlidingMoveGenerator.QUEEN);

    public static List<Move> forColor(ChessMatch match) {
        List<Move> pseudo = new ArrayList<>(100);

        // Generate all pseudo-moves for the given color
        for (int rank = 1; rank < 9; rank++)
            for (int file = 0; file < 8; file++) {
                Position position = PositionFactory.of(rank, file);
                match.board().pieceAt(position).ifPresent(piece -> {
                    if (piece.color() == match.currentColor()) {
                        MoveGenerator gen = generators.get(piece.type());
                        if (gen != null)
                            pseudo.addAll(gen.generate(match.board(), position, piece, match.enPassant().orElse(null)));
                    }
                });
            }
        // Generate all castling moves for the given color
        pseudo.addAll(castlingGen.generate(match.board(), match.currentColor(), match.castleRights()));

        // Filter out checks
        List<Move> legal = new ArrayList<>(pseudo.size());
        for (Move move : pseudo) {
            Board copy = BoardFactory.copy(match.board());
            copy.doMove(move);
            if (!checkCalc.isInCheck(copy, match.currentColor()))
                legal.add(move);
        }
        return legal;
    }
}
