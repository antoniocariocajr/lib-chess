package com.bill.chess.domain.validation;

import com.bill.chess.domain.exception.ChessEngineException;
import com.bill.chess.domain.exception.InvalidBoardException;

public final class FenValidation {

    private FenValidation() {
    }

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

    public static void validateFen(String fen) {
        String[] fenParts = fen.split(" ");
        if (fenParts.length != 6)
            throw new ChessEngineException("fen invalid: " + fen);
    }
}
