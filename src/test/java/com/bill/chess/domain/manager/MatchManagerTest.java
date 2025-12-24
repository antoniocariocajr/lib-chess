package com.bill.chess.domain.manager;

import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.rule.LegalMoveFilter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchManagerTest {

    @Test
    void testUndoRedoCycle() {
        ChessMatch initial = ChessFactory.create();
        MatchManager manager = new MatchManager(initial);

        // 1. apply e4
        Move e4 = findMove(manager.getCurrentMatch(), "e2", "e4");
        manager.applyMove(e4);
        assertEquals(1, manager.getCurrentMatch().board().history().size());
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());

        // 2. undo
        assertTrue(manager.undo());
        assertEquals(0, manager.getCurrentMatch().board().history().size());
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());

        // 3. redo
        assertTrue(manager.redo());
        assertEquals(1, manager.getCurrentMatch().board().history().size());
        assertEquals(e4, manager.getCurrentMatch().board().history().get(0));
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
    }

    private Move findMove(ChessMatch match, String fromNotation, String toNotation) {
        List<Move> legal = LegalMoveFilter.forColor(match);
        return legal.stream()
                .filter(m -> com.bill.chess.domain.factory.PositionFactory.toNotation(m.from()).equals(fromNotation)
                        && com.bill.chess.domain.factory.PositionFactory.toNotation(m.to()).equals(toNotation))
                .findFirst()
                .orElseThrow();
    }
}
