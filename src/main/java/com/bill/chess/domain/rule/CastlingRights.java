package com.bill.chess.domain.rule;

import java.util.EnumSet;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

public final class CastlingRights {
    private CastlingRights(){}

    public static Set<CastleRight> updateCastle(Set<CastleRight> rights, Move move) {
        Set<CastleRight> newRights = EnumSet.copyOf(rights);
        Piece moved = move.pieceMoved();
        if (moved.isKing()) {
            newRights.removeAll(sideRights(moved.color()));
            return newRights;
        }
        if (moved.isRook()) {
            CastleRight right = rookRight(moved.color(), move.from());
            if (right != null)
                newRights.remove(right);
        }
        if (move.captured().isPresent() && move.captured().get().isRook()) {
            CastleRight right = rookRight(move.captured().get().color(), move.to());
            if (right != null)
                newRights.remove(right);
        }
        return newRights;
    }

    private static Set<CastleRight> sideRights(Color color) {
        return color.isWhite() ? Set.of(CastleRight.WHITE_KINGSIDE, CastleRight.WHITE_QUEENSIDE)
                : Set.of(CastleRight.BLACK_KINGSIDE, CastleRight.BLACK_QUEENSIDE);
    }

    private static CastleRight rookRight(Color color, Position position) {
        int rank = color.isWhite() ? 1 : 8;
        if (position.rank() != rank)
            return null;
        return position.file() == 7 ? (color.isWhite() ? CastleRight.WHITE_KINGSIDE : CastleRight.BLACK_KINGSIDE)
                : position.file() == 0 ? (color.isWhite() ? CastleRight.WHITE_QUEENSIDE : CastleRight.BLACK_QUEENSIDE)
                        : null;
    }
}
