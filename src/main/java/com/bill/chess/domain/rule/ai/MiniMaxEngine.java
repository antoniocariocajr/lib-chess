package com.bill.chess.domain.rule.ai;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.BoardTransformer;
import com.bill.chess.domain.rule.LegalMoveFilter;

import java.util.List;


public final class MiniMaxEngine {

    private final int searchDepth;

    public MiniMaxEngine(int depth) {
        this.searchDepth = depth;
    }

    public Move findBestMove(Board board, Color color) {
        List<Move> legalMoves = LegalMoveFilter.forColor(board, color, null, null);

        Move bestMove = null;
        int bestValue = color.isWhite() ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : legalMoves) {
            Board nextBoard = BoardTransformer.apply(board, move);
            int value = minimax(nextBoard, searchDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !color.isWhite());

            if (color.isWhite()) {
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = move;
                }
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0) {
            return EvaluationFunction.evaluate(board);
        }

        Color color = isMaximizing ? Color.WHITE : Color.BLACK;
        List<Move> legalMoves = LegalMoveFilter.forColor(board, color, null, null);

        if (legalMoves.isEmpty()) {
            if (com.bill.chess.domain.rule.InCheckCalculator.isInCheck(board, color)) {
                // Checkmate: value is very bad for the side in check
                return isMaximizing ? -30000 : 30000;
            }
            // Stalemate
            return 0;
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                Board nextBoard = BoardTransformer.apply(board, move);
                int eval = minimax(nextBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                Board nextBoard = BoardTransformer.apply(board, move);
                int eval = minimax(nextBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }
}
