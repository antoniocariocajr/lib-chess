package com.bill.chess.domain.validation;

public final class ValidPositionRule {

    private ValidPositionRule() {
    }

    public static boolean isValid(int rank, int file) {
        return rank >= 1 && rank <= 8 && file >= 0 && file <= 7;
    }

}
