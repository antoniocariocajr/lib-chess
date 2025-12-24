package com.bill.chess.domain.factory;

import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.BoardTransformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exporta uma partida de xadrez para o formato PGN.
 */
public final class PgnExporter {

    private PgnExporter() {
    }

    public static String export(ChessMatch match) {
        StringBuilder pgn = new StringBuilder();

        // Headers (Default values)
        pgn.append("[Event \"Casual Game\"]\n");
        pgn.append("[Site \"?\"]\n");
        pgn.append("[Date \"").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .append("\"]\n");
        pgn.append("[Round \"1\"]\n");
        pgn.append("[White \"White Player\"]\n");
        pgn.append("[Black \"Black Player\"]\n");
        pgn.append("[Result \"*\"]\n"); // * indicates ongoing or unknown result
        pgn.append("\n");

        // Move Text
        Board currentBoard = BoardFactory.create(); // Starting from initial board
        List<Move> history = match.board().history();

        for (int i = 0; i < history.size(); i++) {
            if (i % 2 == 0) {
                pgn.append((i / 2) + 1).append(". ");
            }
            Move move = history.get(i);
            pgn.append(SanConverter.toSan(currentBoard, move)).append(" ");
            currentBoard = BoardTransformer.apply(currentBoard, move);
        }

        pgn.append("*"); // Termination marker

        return pgn.toString();
    }
}
