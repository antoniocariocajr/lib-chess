package com.bill.chess.domain.move;

import java.util.ArrayList;
import java.util.List;

import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;

public final class BoardTransformer {
    private BoardTransformer() {
    }

    public static Board apply(Board old, Move move) {
        Piece[][] newSquares = old.copySquares();
        Position whiteKing = old.whiteKingPos();
        Position blackKing = old.blackKingPos();

        Piece movedPiece = newSquares[move.from().rank()][move.from().file()];

        // 1. basic move
        newSquares[move.to().rank()][move.to().file()] = move.promotion()
                .orElse(movedPiece);
        newSquares[move.from().rank()][move.from().file()] = null;

        // Update king position
        if (movedPiece.isKing()) {
            if (movedPiece.isWhite()) {
                whiteKing = move.to();
            } else {
                blackKing = move.to();
            }
        }

        // 2. special cases
        if (move.isCastling()) {
            int rank = move.from().rank();
            if (move.to().file() == 6) { // kingside
                newSquares[rank][5] = newSquares[rank][7];
                newSquares[rank][7] = null;
            } else { // queenside
                newSquares[rank][3] = newSquares[rank][0];
                newSquares[rank][0] = null;
            }
        }
        if (move.isEnPassant()) { // en passant capture
            newSquares[move.from().rank()][move.to().file()] = null;
        }

        List<Move> newHistory = new ArrayList<>(old.history());
        newHistory.add(move);
        return BoardFactory.of(newSquares, newHistory, whiteKing, blackKing);
    }
}
