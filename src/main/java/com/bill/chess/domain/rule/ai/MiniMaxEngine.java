package com.bill.chess.domain.rule.ai;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.BoardTransformer;
import com.bill.chess.domain.rule.InCheckCalculator;
import com.bill.chess.domain.rule.LegalMoveFilter;

import java.util.ArrayList;
import java.util.List;

public final class MiniMaxEngine {

    private final int searchDepth;

    public MiniMaxEngine(int depth) {
        this.searchDepth = depth;
    }

    public Move findBestMove(Board board, Color color) {
        List<Move> legalMoves = LegalMoveFilter.forColor(board, color, null, null);
        MoveSorter.sortMoves(board, legalMoves);

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
            return quiescenceSearch(board, alpha, beta, isMaximizing);
        }

        Color color = isMaximizing ? Color.WHITE : Color.BLACK;
        List<Move> legalMoves = LegalMoveFilter.forColor(board, color, null, null);

        if (legalMoves.isEmpty()) {
            if (InCheckCalculator.isInCheck(board, color)) {
                // Checkmate: value is very bad for the side in check
                // We add depth to favor shorter mates
                return isMaximizing ? -30000 - depth : 30000 + depth;
            }
            // Stalemate
            return 0;
        }

        MoveSorter.sortMoves(board, legalMoves);

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

    private int quiescenceSearch(Board board, int alpha, int beta, boolean isMaximizing) {
        int standPat = EvaluationFunction.evaluate(board);

        if (isMaximizing) {
            if (standPat >= beta)
                return beta;
            if (alpha < standPat)
                alpha = standPat;
        } else {
            if (standPat <= alpha)
                return alpha;
            if (beta > standPat)
                beta = standPat;
        }

        Color color = isMaximizing ? Color.WHITE : Color.BLACK;
        List<Move> legalMoves = LegalMoveFilter.forColor(board, color, null, null)
                .stream()
                .filter(m -> m.captured().isPresent())
                .toList();

        if (legalMoves.isEmpty()) {
            return standPat;
        }

        // We need a modifiable list to sort
        List<Move> captures = new ArrayList<>(legalMoves);
        MoveSorter.sortMoves(board, captures);

        if (isMaximizing) {
            for (Move move : captures) {
                Board nextBoard = BoardTransformer.apply(board, move);
                int eval = quiescenceSearch(nextBoard, alpha, beta, false);
                if (eval >= beta)
                    return beta;
                if (eval > alpha)
                    alpha = eval;
            }
            return alpha;
        } else {
            for (Move move : captures) {
                Board nextBoard = BoardTransformer.apply(board, move);
                int eval = quiescenceSearch(nextBoard, alpha, beta, true);
                if (eval <= alpha)
                    return alpha;
                if (eval < beta)
                    beta = eval;
            }
            return beta;
        }
    }
}
