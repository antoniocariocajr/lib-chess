package com.bill.chess.domain.validation;

import java.util.Optional;

import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.exception.ChessEngineException;

import static com.bill.chess.domain.validation.ChessValidation.validateLegality;
import static com.bill.chess.domain.validation.ChessValidation.validateTurn;

public final class MoveValidationRule {

    private MoveValidationRule() {
    }

    public static void validateMove(ChessMatch match, Move move) {
        Piece pieceMoved = match.board().pieceAt(move.from())
                .orElseThrow(()-> new ChessEngineException("not found piece in move"));
        Optional<Piece> occupationSquare = match.board().pieceAt(move.to());
        if (occupationSquare.isPresent() && occupationSquare.get().color()==pieceMoved.color())
            throw new ChessEngineException("move not permission");
        validateTurn(pieceMoved,match.currentColor());
        validateLegality(match,move);
    }



}
