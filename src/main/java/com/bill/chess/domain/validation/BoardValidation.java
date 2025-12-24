package com.bill.chess.infra.validation;

import com.bill.chess.domain.model.Piece;
import com.bill.chess.infra.exception.InvalidBoardException;

public class BoardValidation {

    public static void validateFenBoard(String fenBoard) {
        String[] rankStr = fenBoard.split("/");
        if (rankStr.length != 8) {
            throw new InvalidBoardException("Invalid FEN board: " + fenBoard);
        }
        for (String rank : rankStr) {
            int count = 0;
            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    count += c - '0';
                } else {
                    count++;
                }
            }
            if (count != 8) {
                throw new InvalidBoardException("Invalid FEN board: " + fenBoard);
            }
        }
    }

    public static void validateSquares(Piece[][] squares) {
        if (squares.length != 9) {
            throw new InvalidBoardException("Invalid squares: " + squares);
        }
        for (Piece square : squares[0]) {
            if (square != null) {
                throw new InvalidBoardException("Invalid squares: " + squares);
            }
        }
        for (Piece[] rank : squares) {
            if (rank.length != 8) {
                throw new InvalidBoardException("Invalid squares: " + squares);
            }
        }
    }

}
