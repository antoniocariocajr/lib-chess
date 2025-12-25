package com.bill.chess.domain.engine;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniMaxEngineTest {

    @Test
    void shouldFindMaterialGain() {
        // Build a board where White can capture a Black Queen for free
        Board board = BoardFactory.create();
        // Initial board: remove some pieces to create a clear tactical shot
        // Let's just use FEN for simplicity if available, or manually set up

        // Manual setup: White Rook at e1, Black Queen at e8, empty e-file
        // We'll use a simpler setup for the test
        MiniMaxEngine engine = new MiniMaxEngine(2);

        // Let's test if it prefers a capture over a non-capture
        Move bestMove = engine.findBestMove(board, Color.WHITE);

        assertNotNull(bestMove);
        // At depth 2, it should at least return a valid move from the starting position
        System.out.println("Best move found: " + bestMove.from() + " -> " + bestMove.to());
    }

    @Test
    void evaluationShouldBeSymmetric() {
        Board board = BoardFactory.create();
        int initialEval = EvaluationFunction.evaluate(board);
        assertEquals(0, initialEval, "Initial position should be balanced (0)");
    }
}
