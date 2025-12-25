package com.bill.chess.domain.validation;

import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.exception.InvalidBoardException;

import java.util.Arrays;

public final class BoardValidation {
    private BoardValidation() {
    }

    public static void validateSquares(Piece[][] squares) {
        if (squares.length != 9) {
            throw new InvalidBoardException("Invalid squares: " + Arrays.deepToString(squares));
        }
        for (Piece square : squares[0]) {
            if (square != null) {
                throw new InvalidBoardException("Invalid squares: " + Arrays.deepToString(squares));
            }
        }
        for (Piece[] rank : squares) {
            if (rank.length != 8) {
                throw new InvalidBoardException("Invalid squares: " + Arrays.deepToString(squares));
            }
        }
    }

    public static void positionKing(Position position) {
        if (position == null) {
            throw new InvalidBoardException("Not found king position in board");
        }
    }

}
