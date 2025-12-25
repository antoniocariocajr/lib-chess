package com.bill.chess.domain.validation;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.exception.ChessEngineException;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.rule.PieceMoveProvider;

import java.util.List;

import static com.bill.chess.domain.rule.StatusMatchCalculator.calculatorStatus;

public final class ChessValidation {
    private ChessValidation() {
    }

    public static void validateTurn(Piece pieceMoved, Color colorTurn) {
        if (!pieceMoved.color().equals(colorTurn)) {
            throw new ChessEngineException("It is not the turn of the piece");
        }
    }

    public static void ValidateStatus(ChessMatch match) {
        if (match.status() != MatchStatus.IN_PROGRESS) {
            MatchStatus status = calculatorStatus(match);
            if (status != MatchStatus.IN_PROGRESS)
                throw new ChessEngineException("Match is finish");
        }
    }

    public static void validateLegality(ChessMatch match, Move move) {
        List<Move> legalMove = PieceMoveProvider.forPiece(match, move.from());
        if (legalMove.isEmpty())
            throw new ChessEngineException("não eciste movimentos validos para posição inicial");
        if (!legalMove.contains(move))
            throw new ChessEngineException("move invalid");
    }

}
