package com.bill.chess.domain.factory;

import java.util.HashSet;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;

public class CastleFactory {

    public static Set<CastleRight> create() {
        return Set.of(CastleRight.WHITE_KINGSIDE, CastleRight.WHITE_QUEENSIDE, CastleRight.BLACK_KINGSIDE,
                CastleRight.BLACK_QUEENSIDE);
    }

    public static Set<CastleRight> fromFen(String fenCastle) {
        Set<CastleRight> castleRights = new HashSet<>();
        if (fenCastle.equals("-"))
            return castleRights;
        for (char c : fenCastle.toCharArray()) {
            switch (c) {
                case 'K' -> castleRights.add(CastleRight.WHITE_KINGSIDE);
                case 'Q' -> castleRights.add(CastleRight.WHITE_QUEENSIDE);
                case 'k' -> castleRights.add(CastleRight.BLACK_KINGSIDE);
                case 'q' -> castleRights.add(CastleRight.BLACK_QUEENSIDE);
            }
        }
        return castleRights;
    }

    public static String toFen(Set<CastleRight> castleRights) {
        if (castleRights.isEmpty())
            return "-";
        StringBuilder fen = new StringBuilder();
        // Standard FEN order: KQkq
        if (castleRights.contains(CastleRight.WHITE_KINGSIDE))
            fen.append('K');
        if (castleRights.contains(CastleRight.WHITE_QUEENSIDE))
            fen.append('Q');
        if (castleRights.contains(CastleRight.BLACK_KINGSIDE))
            fen.append('k');
        if (castleRights.contains(CastleRight.BLACK_QUEENSIDE))
            fen.append('q');
        return fen.toString();
    }
}
