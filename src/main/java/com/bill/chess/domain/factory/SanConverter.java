package com.bill.chess.domain.factory;

import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.rule.LegalMoveFilter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converte movimentos para Standard Algebraic Notation (SAN).
 */
public final class SanConverter {

    private SanConverter() {
    }

    public static String toSan(Board boardBeforeMove, Move move) {
        if (move.isCastling()) {
            return move.to().file() == 6 ? "O-O" : "O-O-O";
        }

        StringBuilder san = new StringBuilder();
        Piece moved = move.pieceMoved();

        if (moved.isPawn()) {
            if (move.captured().isPresent()) {
                san.append(fileToChar(move.from().file()));
                san.append("x");
            }
            san.append(PositionFactory.toNotation(move.to()));
            if (move.promotion().isPresent()) {
                san.append("=").append(move.pawnPromotion().symbol());
            }
        } else {
            san.append(moved.symbol());

            // Disambiguation
            List<Move> otherMoves = LegalMoveFilter.forColor(boardBeforeMove, moved.color(), null, null)
                    .stream()
                    .filter(m -> !m.equals(move)
                            && m.pieceMoved().type() == moved.type()
                            && m.to().equals(move.to()))
                    .collect(Collectors.toList());

            if (!otherMoves.isEmpty()) {
                boolean sameFile = otherMoves.stream().anyMatch(m -> m.from().file() == move.from().file());
                boolean sameRank = otherMoves.stream().anyMatch(m -> m.from().rank() == move.from().rank());

                if (!sameFile) {
                    san.append(fileToChar(move.from().file()));
                } else if (!sameRank) {
                    san.append(move.from().rank());
                } else {
                    san.append(fileToChar(move.from().file()));
                    san.append(move.from().rank());
                }
            }

            if (move.captured().isPresent()) {
                san.append("x");
            }
            san.append(PositionFactory.toNotation(move.to()));
        }

        return san.toString();
    }

    private static char fileToChar(int file) {
        return (char) ('a' + file);
    }
}
