package com.bill.chess.domain.move;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.model.*;

import java.util.Set;

import static com.bill.chess.domain.rule.CastlingRights.updateCastle;
import static com.bill.chess.domain.rule.EnPassantCalculator.updateEnPassant;
import static com.bill.chess.domain.rule.FullMoveNumber.updateFullMove;
import static com.bill.chess.domain.rule.HalfMoveClock.updateHalfMove;
import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;
import static com.bill.chess.domain.rule.StatusMatchCalculator.calculatorStatus;

public final class MoveApplicator {

    public static  ChessMatch apply(ChessMatch chessMatch, Move move) {
        //performs the move
        Board board = chessMatch.board();
        board.doMove(move);
        //update match
        Color colorNewTurn = chessMatch.currentColor().opposite();
        Set<CastleRight> rights =updateCastle(chessMatch.castleRights(), move);
        Position enPassant = updateEnPassant(move);
        Integer halfMoveClock = updateHalfMove(chessMatch.halfMoveClock(), move);
        MatchStatus status = calculatorStatus(board,colorNewTurn,rights,enPassant,halfMoveClock);
        return new ChessMatch(
                board,
                status,
                colorNewTurn,
                rights,
                enPassant,
                halfMoveClock,
                updateFullMove(chessMatch.fullMoveNumber(), colorNewTurn),
                isInCheck(board,colorNewTurn));
    }

}
