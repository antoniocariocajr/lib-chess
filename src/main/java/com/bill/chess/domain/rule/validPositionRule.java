package com.bill.chess.domain.rule;

import com.bill.chess.domain.model.Position;

public final class validPositionRule {

    private validPositionRule() {
    }

    public static boolean isValid(Position position) {
        return position.rank() >= 1 && position.rank() <= 8 && position.file() >= 0 && position.file() <= 7;
    }

    public static boolean isValid(int rank, int file) {
        return rank >= 1 && rank <= 8 && file >= 0 && file <= 7;
    }

}
