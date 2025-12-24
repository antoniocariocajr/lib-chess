package com.bill.chess.domain.move;

import com.bill.chess.domain.exception.InvalidBoardException;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.exception.ChessEngineException;
import com.bill.chess.domain.rule.LegalMoveFilter;

import static com.bill.chess.domain.validation.ChessValidation.*;

public final class MoveValidator {

    private MoveValidator() {
    }

    public static void validate(ChessMatch match, Move move) {
        ValidateStatus(match);
        validateTurn(move.pieceMoved(),match.currentColor());
        validateLegality(match,move);
        if (move.captured().isPresent() && move.captured().get().color()==move.pieceMoved().color())
            throw new ChessEngineException("move not permission");
        if (!LegalMoveFilter.forColor(match).contains(move))
            throw new InvalidBoardException("Illegal move: " + move);
    }



}
