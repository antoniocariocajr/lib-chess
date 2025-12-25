package com.bill.chess.domain.manager;

import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.MoveApplicator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Gerencia o estado de uma partida de xadrez, permitindo desfazer (Undo) e
 * refazer (Redo) movimentos.
 */
public class MatchManager {

    private ChessMatch currentMatch;
    private final Deque<ChessMatch> history = new ArrayDeque<>();
    private final Deque<Move> redoStack = new ArrayDeque<>();

    public MatchManager(ChessMatch initialMatch) {
        this.currentMatch = initialMatch;
    }

    public void applyMove(Move move) {
        history.push(currentMatch);
        redoStack.clear();
        currentMatch = MoveApplicator.apply(currentMatch, move);
    }

    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }

        var moves = currentMatch.board().history();
        if (!moves.isEmpty()) {
            Move lastMove = moves.getLast();
            redoStack.push(lastMove);
        }

        currentMatch = history.pop();
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }

        Move moveToRedo = redoStack.pop();
        history.push(currentMatch);
        currentMatch = MoveApplicator.apply(currentMatch, moveToRedo);
        return true;
    }

    public ChessMatch getCurrentMatch() {
        return currentMatch;
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
