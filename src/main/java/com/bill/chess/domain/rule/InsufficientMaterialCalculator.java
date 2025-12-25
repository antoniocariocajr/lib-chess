package com.bill.chess.domain.rule;

import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

import java.util.ArrayList;
import java.util.List;

public final class InsufficientMaterialCalculator {

    private InsufficientMaterialCalculator() {
    }

    public static boolean hasInsufficientMaterial(Board board) {
        List<PieceWithPos> whitePieces = new ArrayList<>();
        List<PieceWithPos> blackPieces = new ArrayList<>();

        for (int rank = 1; rank <= 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position position = new Position(rank, file);
                board.pieceAt(position).ifPresent(piece -> {
                    if (piece.color().isWhite()) {
                        whitePieces.add(new PieceWithPos(piece, position));
                    } else {
                        blackPieces.add(new PieceWithPos(piece, position));
                    }
                });
            }
        }

        // 1. King vs King
        if (whitePieces.size() == 1 && blackPieces.size() == 1) {
            return true;
        }

        // 2. King & Bishop vs King or King & Knight vs King
        if (whitePieces.size() == 2 && blackPieces.size() == 1) {
            return isInsufficient(whitePieces);
        }
        if (blackPieces.size() == 2 && whitePieces.size() == 1) {
            return isInsufficient(blackPieces);
        }

        // 3. King & Bishop vs King & Bishop
        if (whitePieces.size() == 2 && blackPieces.size() == 2) {
            PieceWithPos whiteBishop = findBishop(whitePieces);
            PieceWithPos blackBishop = findBishop(blackPieces);

            if (whiteBishop != null && blackBishop != null) {
                // Insufficient if both bishops are on the same color
                return isSameColorSquare(whiteBishop.position, blackBishop.position);
            }
        }

        return false;
    }

    private static boolean isInsufficient(List<PieceWithPos> pieces) {
        for (PieceWithPos pwp : pieces) {
            if (pwp.piece.type() == PieceType.BISHOP || pwp.piece.type() == PieceType.KNIGHT) {
                return true;
            }
        }
        return false;
    }

    private static PieceWithPos findBishop(List<PieceWithPos> pieces) {
        for (PieceWithPos pwp : pieces) {
            if (pwp.piece.type() == PieceType.BISHOP) {
                return pwp;
            }
        }
        return null;
    }

    private static boolean isSameColorSquare(Position p1, Position p2) {
        return (p1.rank() + p1.file()) % 2 == (p2.rank() + p2.file()) % 2;
    }

    private record PieceWithPos(Piece piece, Position position) {
    }
}
