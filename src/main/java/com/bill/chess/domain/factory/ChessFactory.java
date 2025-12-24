package com.bill.chess.domain.factory;

import java.util.ArrayList;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Position;

import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;
import static com.bill.chess.domain.rule.StatusMatchCalculator.calculatorStatus;

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

    public static ChessMatch fromFen(String fen) {
        String[] fenParts = fen.split(" ");
        Board board = BoardFactory.fromFen(fenParts[0], new ArrayList<>());
        Color color = ColorFactory.toColor(fenParts[1]);
        Set<CastleRight> castleRights = CastleFactory.fromFen(fenParts[2]);
        Position enPassantSquare = PositionFactory.fromFen(fenParts[3]);
        Integer halfMoveClock = Integer.parseInt(fenParts[4]);
        Integer fullMoveNumber = Integer.parseInt(fenParts[5]);
        boolean inCheck = isInCheck(board,color);
        MatchStatus status = calculatorStatus(board,color,castleRights,enPassantSquare,halfMoveClock);
        return new ChessMatch(board, status, color, castleRights, enPassantSquare, halfMoveClock,
                fullMoveNumber, inCheck);
    }

    public static String toFen(ChessMatch match) {
        StringBuilder fen = new StringBuilder();
        fen.append(BoardFactory.toFen(match.board()));
        fen.append(" ");
        fen.append(ColorFactory.toSymbol(match.currentColor()));
        fen.append(" ");
        fen.append(CastleFactory.toFen(match.castleRights()));
        fen.append(" ");
        if (match.enPassant().isPresent())
            fen.append(PositionFactory.toNotation(match.enPassant().get()));
        else
            fen.append("-");
        fen.append(" ");
        fen.append(match.halfMoveClock());
        fen.append(" ");
        fen.append(match.fullMoveNumber());
        return fen.toString();
    }

}
