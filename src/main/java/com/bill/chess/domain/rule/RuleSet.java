package com.bill.chess.domain.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.infra.exception.ChessEngineException;

public class RuleSet {
    private RuleSet() {
    }

    public static List<Move> generateLegal(Board board, Color colorSide, Set<CastleRight> rights, Position enPassant) {

        List<Move> pseudo = new ArrayList<>(100);
        for (int rank = 1; rank < 9; rank++)
            for (int file = 0; file < 8; file++) {
                Position position = PositionFactory.of(rank, file);
                board.pieceAt(position).ifPresent(piece -> {
                    if (piece.color() == colorSide) {
                        pseudo.addAll(pseudoMoves(board, position, piece, enPassant));
                    }
                });
            }
        pseudo.addAll(generateCastling(board, colorSide, rights));
        List<Move> legal = new ArrayList<>(pseudo.size());
        for (Move move : pseudo) {
            Board copyBoard = BoardFactory.copy(board);
            copyBoard.doMove(move);
            if (!isInCheck(copyBoard, colorSide))
                legal.add(move);
        }
        return legal;
    }

    public static List<Move> generateLegalInPosition(Board board, Color colorSide, Set<CastleRight> rights,
            Position enPassant, Position position) {

        List<Move> pseudo = new ArrayList<>(100);
        board.pieceAt(position).ifPresent(piece -> {
            if (piece.color() == colorSide) {
                pseudo.addAll(pseudoMoves(board, position, piece, enPassant));
            }
        });
        pseudo.addAll(generateCastling(board, colorSide, rights));
        List<Move> legal = new ArrayList<>(pseudo.size());
        for (Move move : pseudo) {
            Board copyBoard = BoardFactory.copy(board);
            copyBoard.doMove(move);
            if (!isInCheck(copyBoard, colorSide))
                legal.add(move);
        }
        return legal;
    }

    public static boolean isInCheck(Board board, Color colorSide) {
        Position kingPosition = null;
        outer: for (int rank = 1; rank < 9; rank++)
            for (int file = 0; file < 8; file++) {
                Position position = PositionFactory.of(rank, file);
                Optional<Piece> optPiece = board.pieceAt(position);
                if (optPiece.isPresent() && optPiece.get().isKing()
                        && optPiece.get().color() == colorSide) {
                    kingPosition = position;
                    break outer;
                }
            }
        if (kingPosition == null)
            return false;
        return isSquareAttacked(board, kingPosition, colorSide.opposite());
    }

    public static MatchStatus classify(Board board, Color colorSide,
            Set<CastleRight> rights,
            Position enPassant) {
        List<Move> legal = generateLegal(board, colorSide, rights, enPassant);
        if (legal.isEmpty()) {
            if (isInCheck(board, colorSide))
                return colorSide.isWhite() ? MatchStatus.BLACK_WINS : MatchStatus.WHITE_WINS;
            return MatchStatus.STALEMATE;
        }
        return MatchStatus.IN_PROGRESS;
    }

    private static List<Move> pseudoMoves(Board board, Position from, Piece piece, Position enPassant) {
        List<Move> list = new ArrayList<>(28);
        Color colorSide = piece.color();
        switch (piece.type()) {
            case PAWN -> list.addAll(pawnMoves(board, from, colorSide, enPassant));
            case KNIGHT -> list.addAll(knightMoves(board, from, colorSide));
            case BISHOP -> list.addAll(sliding(board, from, colorSide, bishopDirs));
            case ROOK -> list.addAll(sliding(board, from, colorSide, rookDirs));
            case QUEEN -> list.addAll(sliding(board, from, colorSide, queenDirs));
            case KING -> list.addAll(kingMoves(board, from, colorSide));
        }
        return list;
    }

    private static List<Move> pawnMoves(Board board, Position from, Color colorSide, Position enPassant) {
        Piece pawn = board.pieceAt(from).orElseThrow(() -> new ChessEngineException("No piece at " + from));
        List<Move> moves = new ArrayList<>(8);
        int rankDirection = colorSide.isWhite() ? 1 : -1;
        int startRank = colorSide.isWhite() ? 2 : 7;
        int newRank = from.rank() + rankDirection;
        if (validPositionRule.isValid(newRank, from.file())) {
            Position to = PositionFactory.of(newRank, from.file());
            Optional<Piece> pieceAtTo = board.pieceAt(to);
            // move forward
            if (pieceAtTo.isEmpty()) {
                addPawn(moves, from, PositionFactory.of(newRank, from.file()), colorSide, null);
                // move forward two squares
                if (from.rank() == startRank) {
                    newRank = from.rank() + 2 * rankDirection;
                    to = PositionFactory.of(newRank, from.file());
                    pieceAtTo = board.pieceAt(to);
                    if (pieceAtTo.isEmpty())
                        moves.add(MoveFactory.enPassant(from, to, pawn));
                }
            }
        }
        // capture
        newRank = from.rank() + rankDirection;
        for (int fileDirection : new int[] { -1, 1 }) {
            int newFile = from.file() + fileDirection;
            if (!validPositionRule.isValid(newRank, newFile))
                continue;
            Position to = PositionFactory.of(newRank, newFile);
            Optional<Piece> opposingPiece = board.pieceAt(to);
            if (opposingPiece.isPresent() && opposingPiece.get().color() != colorSide)
                addPawn(moves, from, to, colorSide, opposingPiece.get());
            if (to.equals(enPassant) && enPassant != null) {
                moves.add(MoveFactory.capture(from, to, new Piece(PieceType.PAWN, colorSide.opposite()), pawn));
            }
        }
        return moves;
    }

    private static void addPawn(List<Move> moves, Position from, Position to, Color colorSide, Piece capture) {
        if (to.rank() == 1 || to.rank() == 8) {
            for (PieceType pr : new PieceType[] { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT })
                if (capture != null) {
                    moves.add(MoveFactory.promotionAndCapture(from, to, capture,
                            new Piece(pr, colorSide), new Piece(PieceType.PAWN, colorSide)));
                } else {
                    moves.add(MoveFactory.promotion(from, to, new Piece(pr, colorSide),
                            new Piece(PieceType.PAWN, colorSide)));
                }
        } else {
            moves.add(capture != null ? MoveFactory.capture(from, to, capture, new Piece(PieceType.PAWN, colorSide))
                    : MoveFactory.quiet(from, to, new Piece(PieceType.PAWN, colorSide)));
        }
    }

    private static List<Move> knightMoves(Board board, Position from, Color colorSide) {
        int[] rankDirection = { -2, -2, -1, -1, 1, 1, 2, 2 };
        int[] fileDirection = { -1, 1, -2, 2, -2, 2, -1, 1 };
        return stepMoves(board, from, colorSide, rankDirection, fileDirection);
    }

    private static List<Move> kingMoves(Board board, Position from, Color colorSide) {
        int[] rankDirection = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] fileDirection = { -1, 0, 1, -1, 1, -1, 0, 1 };
        return stepMoves(board, from, colorSide, rankDirection, fileDirection);
    }

    private static List<Move> stepMoves(Board board, Position from, Color colorSide, int[] rankDirection,
            int[] fileDirection) {
        Piece piece = board.pieceAt(from).orElseThrow(() -> new ChessEngineException("No piece at " + from));
        List<Move> moves = new ArrayList<>(rankDirection.length);
        for (int i = 0; i < rankDirection.length; i++) {
            int rankTrajectory = from.rank() + rankDirection[i];
            int fileTrajectory = from.file() + fileDirection[i];
            if (!validPositionRule.isValid(rankTrajectory, fileTrajectory))
                continue;
            Position to = PositionFactory.of(rankTrajectory, fileTrajectory);
            Optional<Piece> opposingPiece = board.pieceAt(to);
            if (opposingPiece.isEmpty())
                moves.add(MoveFactory.quiet(from, to, piece));
            else if (opposingPiece.get().color() != colorSide)
                moves.add(MoveFactory.capture(from, to, opposingPiece.get(), piece));
        }
        return moves;
    }

    private static final int[][] bishopDirs = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
    private static final int[][] rookDirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private static final int[][] queenDirs = Arrays.copyOf(bishopDirs, 8);// Position 0-3 (Bishop)
    static {
        System.arraycopy(rookDirs, 0, queenDirs, 4, 4);// Position 4-7 (Rook)
    }

    private static List<Move> sliding(Board board, Position from, Color colorSide, int[][] directions) {
        Piece piece = board.pieceAt(from).orElseThrow(() -> new ChessEngineException("No piece at " + from));
        List<Move> moves = new ArrayList<>(28);
        for (int[] direction : directions) {
            int rank = from.rank(), file = from.file();
            while (true) {
                rank += direction[0];
                file += direction[1];
                if (!validPositionRule.isValid(rank, file))
                    break;
                Position to = PositionFactory.of(rank, file);
                Optional<Piece> opposingPiece = board.pieceAt(to);
                if (opposingPiece.isEmpty())
                    moves.add(MoveFactory.quiet(from, to, piece));
                else {
                    if (opposingPiece.get().color() != colorSide)
                        moves.add(MoveFactory.capture(from, to, opposingPiece.get(), piece));
                    break;
                }
            }
        }
        return moves;
    }

    private static List<Move> generateCastling(Board board, Color colorSide, Set<CastleRight> rights) {
        List<Move> moves = new ArrayList<>(2);
        boolean ks = colorSide.isWhite() ? rights.contains(CastleRight.WHITE_KINGSIDE)
                : rights.contains(CastleRight.BLACK_KINGSIDE);
        boolean qs = colorSide.isWhite() ? rights.contains(CastleRight.WHITE_QUEENSIDE)
                : rights.contains(CastleRight.BLACK_QUEENSIDE);
        int rank = colorSide.isWhite() ? 1 : 8;
        Position king = PositionFactory.of(rank, 4);
        Optional<Piece> kingPiece = board.pieceAt(king);
        if (kingPiece.isEmpty() || !kingPiece.get().isKing())
            return moves;
        if (!isInCheck(board, colorSide)) {
            // Kingside
            if (ks && board.pieceAt(PositionFactory.of(rank, 5)).isEmpty()
                    && board.pieceAt(PositionFactory.of(rank, 6)).isEmpty()
                    && !isSquareAttacked(board, PositionFactory.of(rank, 5), colorSide.opposite())
                    && !isSquareAttacked(board, PositionFactory.of(rank, 6), colorSide.opposite())) {
                moves.add(MoveFactory.castle(king, PositionFactory.of(rank, 6), board.pieceAt(king).orElse(null)));
            }
            // Queenside
            if (qs && board.pieceAt(PositionFactory.of(rank, 3)).isEmpty()
                    && board.pieceAt(PositionFactory.of(rank, 2)).isEmpty()
                    && board.pieceAt(PositionFactory.of(rank, 1)).isEmpty()
                    && !isSquareAttacked(board, PositionFactory.of(rank, 3), colorSide.opposite())
                    && !isSquareAttacked(board, PositionFactory.of(rank, 2), colorSide.opposite())) {
                moves.add(MoveFactory.castle(king, PositionFactory.of(rank, 2), board.pieceAt(king).orElse(null)));
            }
        }
        return moves;
    }

    private static boolean isSquareAttacked(Board board, Position square, Color colorOpponent) {
        // Pawn
        int direction = colorOpponent.isWhite() ? -1 : 1;
        for (int fileDirection : new int[] { -1, 1 }) {
            int pseudoRank = square.rank() + direction, pseudoFile = square.file() + fileDirection;
            if (validPositionRule.isValid(pseudoRank, pseudoFile)) {
                Optional<Piece> piece = board.pieceAt(PositionFactory.of(pseudoRank, pseudoFile));
                if (piece.isPresent())
                    if (piece.get().isPawn() && piece.get().color() == colorOpponent)
                        return true;
            }
        }
        // Knight
        int[] rankDirection = { -2, -2, -1, -1, 1, 1, 2, 2 };
        int[] fileDirection = { -1, 1, -2, 2, -2, 2, -1, 1 };
        for (int i = 0; i < rankDirection.length; i++) {
            int pseudoRank = square.rank() + rankDirection[i];
            int pseudoFile = square.file() + fileDirection[i];
            if (!validPositionRule.isValid(pseudoRank, pseudoFile))
                continue;
            Position to = PositionFactory.of(pseudoRank, pseudoFile);
            Optional<Piece> opposingPiece = board.pieceAt(to);
            if (opposingPiece.isPresent())
                if (opposingPiece.get().isKnight() && opposingPiece.get().color() == colorOpponent)
                    return true;
        }
        // Sliding (Bishop, Rook, Queen)
        for (int[] d : queenDirs) {
            int pseudoRank = square.rank() + d[0], pseudoFile = square.file() + d[1];
            while (validPositionRule.isValid(pseudoRank, pseudoFile)) {
                Position to = PositionFactory.of(pseudoRank, pseudoFile);
                Optional<Piece> opposingPiece = board.pieceAt(to);
                if (opposingPiece.isPresent()) {
                    Piece piece = opposingPiece.get();
                    if (piece.color() == colorOpponent) {
                        if ((piece.isBishop() || piece.isQueen())
                                && (d[0] != 0 && d[1] != 0))
                            return true;
                        if ((piece.isRook() || piece.isQueen())
                                && (d[0] == 0 || d[1] == 0))
                            return true;
                    }
                    break;
                }
                pseudoRank += d[0];
                pseudoFile += d[1];
            }
        }
        // King
        int[] rankDirection2 = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] fileDirection2 = { -1, 0, 1, -1, 1, -1, 0, 1 };
        for (int i = 0; i < rankDirection2.length; i++) {
            int pseudoRank = square.rank() + rankDirection2[i];
            int pseudoFile = square.file() + fileDirection2[i];
            if (!validPositionRule.isValid(pseudoRank, pseudoFile))
                continue;
            Position to = PositionFactory.of(pseudoRank, pseudoFile);
            Optional<Piece> opposingPiece = board.pieceAt(to);
            if (opposingPiece.isPresent())
                if (opposingPiece.get().isKing() && opposingPiece.get().color() == colorOpponent)
                    return true;
        }
        return false;
    }

}
