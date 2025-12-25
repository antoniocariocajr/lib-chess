package com.bill.chess.domain.rule;

import com.bill.chess.domain.converter.FenConverter;
import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.CastleFactory;
import com.bill.chess.domain.factory.ColorFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Position;

import java.util.Set;

public final class RepetitionDetector {

    private RepetitionDetector() {
    }

    /**
     * Generates a key for the position to track repetitions.
     * The key includes: piece placement, side to move, castling rights, and en
     * passant square.
     * Half-move clock and fullmove number are excluded.
     */
    public static String createPositionKey(Board board, Color turn, Set<CastleRight> rights, Position enPassant) {
        StringBuilder key = new StringBuilder();
        key.append(FenConverter.boardToFen(board));
        key.append(" ");
        key.append(ColorFactory.toSymbol(turn));
        key.append(" ");
        key.append(CastleFactory.toFen(rights));
        key.append(" ");
        if (enPassant != null) {
            key.append(PositionFactory.toNotation(enPassant));
        } else {
            key.append("-");
        }
        return key.toString();
    }

    public static boolean isThreefoldRepetition(java.util.List<String> history) {
        if (history == null || history.size() < 3)
            return false;

        String current = history.get(history.size() - 1);
        int count = 0;
        for (String pos : history) {
            if (pos.equals(current)) {
                count++;
            }
        }
        return count >= 3;
    }
}
