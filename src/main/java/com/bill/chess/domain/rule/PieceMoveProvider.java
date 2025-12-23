package com.bill.chess.domain.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.generator.CastlingGenerator;
import com.bill.chess.domain.generator.MoveGenerator;
import com.bill.chess.domain.generator.PawnMoveGenerator;
import com.bill.chess.domain.generator.SlidingMoveGenerator;
import com.bill.chess.domain.generator.StepMoveGenerator;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

public final class PieceMoveProvider {

    private static final InCheckCalculator CHECK = new InCheckCalculator();
    private static final Map<PieceType, MoveGenerator> GEN = Map.of(
            PieceType.PAWN, new PawnMoveGenerator(),
            PieceType.KNIGHT, StepMoveGenerator.KNIGHT,
            PieceType.KING, StepMoveGenerator.KING,
            PieceType.BISHOP, SlidingMoveGenerator.BISHOP,
            PieceType.ROOK, SlidingMoveGenerator.ROOK,
            PieceType.QUEEN, SlidingMoveGenerator.QUEEN);

    // legal moves for a piece
    public static List<Move> forPiece(ChessMatch match,
            Position pos) {
        Optional<Piece> piece = match.board().pieceAt(pos);
        if (piece.isEmpty() || piece.get().color() != match.currentColor()) {
            return List.of();
        }
        return piece.map(p -> {
            MoveGenerator gen = GEN.get(p.type());

            List<Move> pseudo = gen.generate(match.board(), pos, piece.get(), match.enPassant().orElse(null));

            // add castling if king
            if (piece.get().isKing()) {
                pseudo.addAll(
                        new CastlingGenerator().generate(match.board(), piece.get().color(), match.castleRights()));
            }

            // filter check
            List<Move> legal = new ArrayList<>(pseudo.size());
            for (Move m : pseudo) {
                Board copy = BoardFactory.copy(match.board());
                copy.doMove(m);
                if (!CHECK.isInCheck(copy, match.currentColor()))
                    legal.add(m);
            }
            return legal;
        })
                .orElse(List.of());
    }
}
