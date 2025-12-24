package com.bill.chess.domain.rule;

import java.util.Optional;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.validation.ValidPositionRule;

public final class AttackDetector {
    private AttackDetector(){}

    public static boolean isSquareAttacked(Board board, Position square, Color attacker) {
        return pawnAttack(board, square, attacker)
                || knightAttack(board, square, attacker)
                || slidingAttack(board, square, attacker)
                || kingAttack(board, square, attacker);
    }

    private static boolean pawnAttack(Board board, Position square, Color attacker) {
        int direction = attacker.isWhite() ? -1 : 1;
        for (int fileDirection : new int[] { -1, 1 }) {
            int rank = square.rank() + direction, file = square.file() + fileDirection;
            if (!ValidPositionRule.isValid(rank, file))
                continue;
            Optional<Piece> piece = board.pieceAt(PositionFactory.of(rank, file));
            if (piece.isPresent() && piece.get().isPawn() && piece.get().color() == attacker)
                return true;
        }
        return false;
    }

    private static boolean knightAttack(Board board, Position square, Color attacker) {
        int[] dr = { -2, -2, -1, -1, 1, 1, 2, 2 }, df = { -1, 1, -2, 2, -2, 2, -1, 1 };
        for (int i = 0; i < dr.length; i++) {
            int rank = square.rank() + dr[i], file = square.file() + df[i];
            if (!ValidPositionRule.isValid(rank, file))
                continue;
            Optional<Piece> piece = board.pieceAt(PositionFactory.of(rank, file));
            if (piece.isPresent() && piece.get().isKnight() && piece.get().color() == attacker)
                return true;
        }
        return false;
    }

    private static boolean slidingAttack(Board board, Position square, Color attacker) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        for (int[] direction : directions) {
            int rank = square.rank() + direction[0], file = square.file() + direction[1];
            while (ValidPositionRule.isValid(rank, file)) {
                Position position = PositionFactory.of(rank, file);
                Optional<Piece> pieceOptional = board.pieceAt(position);
                if (pieceOptional.isPresent()) {
                    Piece piece = pieceOptional.get();
                    if (piece.color() != attacker)
                        break;
                    boolean diagonal = direction[0] != 0 && direction[1] != 0;
                    if ((diagonal && (piece.isBishop() || piece.isQueen()))
                            || (!diagonal && (piece.isRook() || piece.isQueen())))
                        return true;
                    break;
                }
                rank += direction[0];
                file += direction[1];
            }
        }
        return false;
    }

    private static boolean kingAttack(Board board, Position square, Color attacker) {
        int[] rankDirections = { -1, -1, -1, 0, 0, 1, 1, 1 }, fileDirections = { -1, 0, 1, -1, 1, -1, 0, 1 };
        for (int i = 0; i < rankDirections.length; i++) {
            int rank = square.rank() + rankDirections[i], file = square.file() + fileDirections[i];
            if (!ValidPositionRule.isValid(rank, file))
                continue;
            Optional<Piece> piece = board.pieceAt(PositionFactory.of(rank, file));
            if (piece.isPresent() && piece.get().isKing() && piece.get().color() == attacker)
                return true;
        }
        return false;
    }
}
