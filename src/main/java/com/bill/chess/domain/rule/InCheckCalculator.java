package com.bill.chess.domain.rule;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Position;

import static com.bill.chess.domain.rule.AttackDetector.isSquareAttacked;

public final class InCheckCalculator {
    private InCheckCalculator() {
    }

    public static boolean isInCheck(Board board, Color side) {
        Position kingPos = board.kingPos(side);
        return kingPos != null && isSquareAttacked(board, kingPos, side.opposite());
    }
}
