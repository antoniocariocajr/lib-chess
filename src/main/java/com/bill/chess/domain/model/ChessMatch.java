package com.bill.chess.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;

public record ChessMatch(
        Board board,
        MatchStatus status,
        Color currentColor,
        Set<CastleRight> castleRights,
        Position enPassantSquare,
        Integer halfMoveClock,
        Integer fullMoveNumber,
        boolean inCheck,
        List<String> positionHistory

) {
    public ChessMatch {
        if (positionHistory == null) {
            positionHistory = Collections.emptyList();
        }
    }

    public Optional<Position> enPassant() {
        return Optional.ofNullable(enPassantSquare);
    }

}
