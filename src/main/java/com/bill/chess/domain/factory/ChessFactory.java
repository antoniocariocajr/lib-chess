package com.bill.chess.domain.factory;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.rule.RepetitionDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChessFactory {

    public static ChessMatch create() {
        Board board = BoardFactory.create();
        Set<CastleRight> rights = CastleFactory.create();
        String initialKey = RepetitionDetector.createPositionKey(board, Color.WHITE, rights, null);
        List<String> history = new ArrayList<>();
        history.add(initialKey);

        return new ChessMatch(
                board,
                MatchStatus.IN_PROGRESS,
                Color.WHITE,
                rights,
                null,
                0,
                1,
                false,
                history);
    }

}
