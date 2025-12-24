package com.bill.chess.domain.factory;

import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.MoveApplicator;
import com.bill.chess.domain.rule.LegalMoveFilter;

/**
 * Carrega uma partida de xadrez a partir de uma string PGN.
 */
public final class PgnLoader {

    private PgnLoader() {
    }

    public static ChessMatch load(String pgn) {
        ChessMatch match = ChessFactory.create();

        // Simple regex to extract move tokens (ignoring headers, move numbers, and
        // symbols like +, #, !, ?)
        // This is a simplified parser for basic SAN.
        String moveText = pgn.replaceAll("\\[.*?\\]", "").replaceAll("\\d+\\.", "").replaceAll("\\+", "")
                .replaceAll("#", "").trim();
        String[] tokens = moveText.split("\\s+");

        for (String token : tokens) {
            if (token.equals("*") || token.equals("1-0") || token.equals("0-1") || token.equals("1/2-1/2")) {
                break;
            }

            Move move = findMoveForSan(match, token);
            if (move == null) {
                throw new IllegalArgumentException(
                        "Could not parse move: " + token + " in position: " + ChessFactory.toFen(match));
            }
            match = MoveApplicator.apply(match, move);
        }

        return match;
    }

    private static Move findMoveForSan(ChessMatch match, String sanToken) {
        // Remove annotation symbols from token
        String cleanToken = sanToken.replaceAll("[+#?!]", "");

        return LegalMoveFilter.forColor(match)
                .stream()
                .filter(move -> SanConverter.toSan(match.board(), move).replaceAll("[+#]", "").equals(cleanToken))
                .findFirst()
                .orElse(null);
    }
}
