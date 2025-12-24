package com.bill.chess.domain.factory;

import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.move.MoveApplicator;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.rule.LegalMoveFilter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PgnTest {

    @Test
    void testExportAndLoadConsistency() {
        // 1. Play a short game
        ChessMatch match = ChessFactory.create();

        // 1. e4
        match = applySanMove(match, "e4");
        // 1... e5
        match = applySanMove(match, "e5");
        // 2. Nf3
        match = applySanMove(match, "Nf3");
        // 2... Nc6
        match = applySanMove(match, "Nc6");
        // 3. Bb5 (Ruy Lopez)
        match = applySanMove(match, "Bb5");

        String originalFen = ChessFactory.toFen(match);

        // 2. Export to PGN
        String pgn = PgnExporter.export(match);
        System.out.println("Generated PGN:\n" + pgn);

        // 3. Load from PGN
        ChessMatch loadedMatch = PgnLoader.load(pgn);
        String loadedFen = ChessFactory.toFen(loadedMatch);

        // 4. Verify FENs match
        assertEquals(originalFen, loadedFen);
    }

    private ChessMatch applySanMove(ChessMatch match, String san) {
        List<Move> legal = LegalMoveFilter.forColor(match);
        for (Move move : legal) {
            if (SanConverter.toSan(match.board(), move).equals(san)) {
                return MoveApplicator.apply(match, move);
            }
        }
        throw new IllegalArgumentException("Move not found: " + san);
    }
}
