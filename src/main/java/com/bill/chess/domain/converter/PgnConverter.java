package com.bill.chess.domain.converter;

import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.BoardTransformer;
import com.bill.chess.domain.move.MoveApplicator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles PGN (Portable Game Notation) conversions for chess matches.
 */
public final class PgnConverter {

    private PgnConverter() {
    }

    /**
     * Exports a chess game to PGN format.
     */
    public static String toPgn(ChessMatch match) {
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

    /**
     * Loads a chess game from a PGN String.
     */
    public static ChessMatch fromPgn(String pgn) {
        ChessMatch match = ChessFactory.create();

        // Simple regex to extract move tokens (ignoring headers, move numbers, and
        // symbols like +, #, !, ?)
        // This is a simplified parser for basic SAN.
        String moveText = pgn.replaceAll("\\[.*?\\]", "").replaceAll("\\d+\\.", "").replaceAll("\\+", "")
                .replaceAll("#", "").trim();
        String[] tokens = moveText.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty())
                continue;
            if (token.equals("*") || token.equals("1-0") || token.equals("0-1") || token.equals("1/2-1/2")) {
                break;
            }

            Move move = SanConverter.fromSan(token, match);
            match = MoveApplicator.apply(match, move);
        }

        return match;
    }

}
