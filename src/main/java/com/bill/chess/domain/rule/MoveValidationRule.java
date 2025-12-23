package com.bill.chess.domain.rule;

import java.util.Optional;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.infra.validation.PieceValidation;

public final class MoveValidationRule {

    private MoveValidationRule() {
    }

    public static void validateTurn(Piece pieceMoved, Color colorTurn) {
        if (!pieceMoved.color().equals(colorTurn)) {
            throw new IllegalArgumentException("It is not the turn of the piece");
        }
    }

    public static void validateMove(Board board, Move move) {
        Optional<Piece> pieceMoved = board.pieceAt(move.from());
        PieceValidation.validatePiece(pieceMoved.get());
    }

}
