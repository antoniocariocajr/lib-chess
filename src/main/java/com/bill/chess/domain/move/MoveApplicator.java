package com.bill.chess.domain.move;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.*;

import static com.bill.chess.domain.move.MoveEnricher.fromMove;
import static com.bill.chess.domain.rule.CastlingRights.updateCastle;
import static com.bill.chess.domain.rule.EnPassantCalculator.updateEnPassant;
import static com.bill.chess.domain.rule.FullMoveNumber.updateFullMove;
import static com.bill.chess.domain.rule.HalfMoveClock.updateHalfMove;
import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;
import static com.bill.chess.domain.rule.StatusMatchCalculator.calculatorStatus;
import static com.bill.chess.domain.validation.ChessValidation.ValidateStatus;
import static com.bill.chess.domain.validation.MoveValidationRule.validateMove;

public final class MoveExecutor {

    public static  ChessMatch executeMove(ChessMatch chessMatch, Move move) {
        //valid move
        ValidateStatus(chessMatch);
        Move moveEnricher = fromMove(chessMatch.board(),move);
        validateMove(chessMatch, moveEnricher);
        //performs the move
        Board board = chessMatch.board();
        board.doMove(moveEnricher);
        //update match
        Color colorNewTurn = chessMatch.currentColor().opposite();
        return new ChessMatch(
                board,
                calculatorStatus(chessMatch),
                colorNewTurn,
                updateCastle(chessMatch.castleRights(), move),
                updateEnPassant(move),
                updateHalfMove(chessMatch.halfMoveClock(), move),
                updateFullMove(chessMatch.fullMoveNumber(), colorNewTurn),
                isInCheck(board,colorNewTurn));
    }

}
