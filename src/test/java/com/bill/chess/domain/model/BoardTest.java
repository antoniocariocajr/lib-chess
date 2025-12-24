package com.bill.chess.domain.model;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.move.BoardTransformer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void doMoveShouldReturnNewBoardAndNotMutateOriginal() {
        // Setup initial board
        Board initialBoard = BoardFactory.create();
        Position from = PositionFactory.fromNotation("e2");
        Position to = PositionFactory.fromNotation("e4");
        Piece pawn = initialBoard.pieceAt(from).orElseThrow();
        Move move = MoveFactory.fromUci("e2e4", pawn);

        // Execute move
        Board nextBoard = BoardTransformer.apply(initialBoard, move);

        // Verify next board has the move executed
        assertNotSame(initialBoard, nextBoard);
        assertTrue(nextBoard.pieceAt(to).isPresent());
        assertEquals(PieceType.PAWN, nextBoard.pieceAt(to).get().type());
        assertFalse(nextBoard.pieceAt(from).isPresent());
        assertEquals(1, nextBoard.history().size());

        // Verify initial board is unchanged
        assertTrue(initialBoard.pieceAt(from).isPresent());
        assertEquals(PieceType.PAWN, initialBoard.pieceAt(from).get().type());
        assertFalse(initialBoard.pieceAt(to).isPresent());
        assertEquals(0, initialBoard.history().size());
    }
}
