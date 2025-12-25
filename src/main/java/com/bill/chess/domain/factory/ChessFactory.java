package com.bill.chess.domain.factory;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;

public class ChessFactory {

    public static ChessMatch create() {
        Board board = BoardFactory.create();
        return new ChessMatch(
                board,
                MatchStatus.IN_PROGRESS,
                Color.WHITE,
                CastleFactory.create(),
                null,
                0,
                1,
                false);
    }

}
