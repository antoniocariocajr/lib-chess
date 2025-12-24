package com.bill.chess.domain.rule;

import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Position;

public final class EnPassantCalculator {
    private EnPassantCalculator(){}

    public static Position updateEnPassant(Move move) {
        if (!move.pieceMoved().isPawn())
            return null;
        Position from = move.from();
        Position to = move.to();
        if (to.rank() != 3 && to.rank() != 6)
            return null;
        int dr = Math.abs(to.rank() - from.rank());
        if (dr == 2) {
            return PositionFactory.of(to.rank() == 3 ? 2 : 7, from.file());
        }
        return null;
    }
}
