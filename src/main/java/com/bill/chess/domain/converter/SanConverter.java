package com.bill.chess.domain.converter;

import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.rule.LegalMoveFilter;

import java.util.List;

/**
 * Convert moves to Standard Algebraic Notation (SAN).
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
                san.append(move.from().fileToChar());
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
                    .toList();

            if (!otherMoves.isEmpty()) {
                boolean sameFile = otherMoves.stream().anyMatch(m -> m.from().file() == move.from().file());
                boolean sameRank = otherMoves.stream().anyMatch(m -> m.from().rank() == move.from().rank());

                if (!sameFile) {
                    san.append(move.from().fileToChar());
                } else if (!sameRank) {
                    san.append(move.from().rank());
                } else {
                    san.append(move.from().fileToChar());
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

    public static Move fromSan(String san, ChessMatch match) {
        String cleanSan = san.replaceAll("[+#?!]", "");

        return LegalMoveFilter.forColor(match)
                .stream()
                .filter(move -> toSan(match.board(), move).replaceAll("[+#]", "").equals(cleanSan))
                .findFirst()
                .orElse(null);
    }
}
