package com.bill.chess.domain.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.validation.ValidPositionRule;

public final class PawnMoveGenerator implements MoveGenerator {

    @Override
    public List<Move> generate(Board board, Position from, Piece piece, Position enPassant) {
        List<Move> moves = new ArrayList<>(8);
        Color color = piece.color();
        // displacement file for captures
        int[] fileDeltas = { -1, 1 };
        // displacement rank for single move and captures
        int rankDelta = color.isWhite() ? 1 : -1;
        // rank in starting position
        int startRank = color.isWhite() ? 2 : 7;

        // single move
        int newRank = from.rank() + rankDelta;
        if (ValidPositionRule.isValid(newRank, from.file())) {
            Position to = PositionFactory.of(newRank, from.file());
            if (board.pieceAt(to).isEmpty()) {
                addPawnMoves(moves, from, to, piece, null);
                // double move if in starting position
                if (from.rank() == startRank) {
                    int rankDouble = from.rank() + 2 * rankDelta;
                    Position toDouble = PositionFactory.of(rankDouble, from.file());
                    if (board.pieceAt(toDouble).isEmpty())
                        moves.add(MoveFactory.quiet(from, toDouble, piece));
                }
            }
        }
        // captures + en-passant
        for (int fileDelta : fileDeltas) {
            int capFile = from.file() + fileDelta;
            int capRank = from.rank() + rankDelta;
            if (!ValidPositionRule.isValid(capRank, capFile))
                continue;
            Position to = PositionFactory.of(capRank, capFile);

            Optional<Piece> victim = board.pieceAt(to);
            if (victim.isPresent() && victim.get().color() != color)
                addPawnMoves(moves, from, to, piece, victim.get());

            if (to.equals(enPassant))
                moves.add(MoveFactory.enPassant(from, to,
                        new Piece(PieceType.PAWN, color.opposite()), piece));
        }
        return moves;
    }

    // checks if the move is a potential promotion and adds it to the list
    private void addPawnMoves(List<Move> moves, Position from, Position to,
            Piece pawn, Piece captured) {
        if (to.rank() == 1 || to.rank() == 8) {
            PieceType[] potentialPromotions = { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP,
                    PieceType.KNIGHT };
            for (PieceType typePromotion : potentialPromotions) {
                Piece promoted = new Piece(typePromotion, pawn.color());
                if (captured != null)
                    moves.add(MoveFactory.promotionAndCapture(from, to, captured, promoted, pawn));
                else
                    moves.add(MoveFactory.promotion(from, to, promoted, pawn));
            }
        } else {
            moves.add(captured == null
                    ? MoveFactory.quiet(from, to, pawn)
                    : MoveFactory.capture(from, to, captured, pawn));
        }
    }
}
