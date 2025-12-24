package com.bill.chess.domain.rule;

import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Position;

public final class EnPassantCalculator {
    private EnPassantCalculator() {
    }

    public static Position updateEnPassant(Move move) {
        if (!move.pieceMoved().isPawn())
            return null;
        Position from = move.from();
        Position to = move.to();
        if (to.rank() != 4 && to.rank() != 5)
            return null;
        int dr = Math.abs(to.rank() - from.rank());
        if (dr == 2) {
            return PositionFactory.of(to.rank() == 4 ? 3 : 6, from.file());
        }
        return null;
    }
}
