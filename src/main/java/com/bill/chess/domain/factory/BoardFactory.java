package com.bill.chess.domain.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.infra.validation.BoardValidation;

public class BoardFactory {

    public static Board of(Piece[][] squares, List<Move> history) {

        BoardValidation.validateSquares(squares);

        return new Board(squares, new ArrayList<>(history));
    }

    public static Board create() {

        Piece[][] squares = new Piece[9][8];

        initializeBoard(squares);

        return of(squares, new ArrayList<>());
    }

    public static Board copy(Board board) {

        Piece[][] newSquares = new Piece[9][8];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(board.squares()[r], 0, newSquares[r], 0, 8);
        }

        return of(newSquares, board.history());
    }

    public static Board fromFen(String fenBoard, List<Move> history) {

        BoardValidation.validateFenBoard(fenBoard);

        Piece[][] squares = new Piece[9][8];
        String[] rankStr = fenBoard.split("/");
        for (int r = 0; r <= 7; r++) {
            String row = rankStr[7 - r];
            int file = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    file += c - '0';
                } else {
                    squares[r + 1][file] = PieceFactory.toPiece(c);
                    file++;
                }
            }
        }

        return of(squares, history);
    }

    public static String toFen(Board board) {
        StringBuilder boardFen = new StringBuilder();
        for (int rank = 8; rank >= 1; rank--) {
            int empty = 0;
            for (int file = 0; file < 8; file++) {
                Optional<Piece> op = board.pieceAt(new Position(rank, file));
                if (op.isEmpty()) {
                    empty++;
                } else {
                    if (empty > 0) {
                        boardFen.append(empty);
                        empty = 0;
                    }
                    boardFen.append(PieceFactory.toUnicode(op.get()));
                }
            }
            if (empty > 0)
                boardFen.append(empty);
            if (rank > 1)
                boardFen.append('/');
        }
        return boardFen.toString();
    }

    private static void initializeBoard(Piece[][] squares) {

        for (int rank = 0; rank < 9; rank++) {
            for (int file = 0; file < 8; file++) {
                squares[rank][file] = null;
            }
        }
        setupBackRank(squares, 1, Color.WHITE);
        setupPawns(squares, 2, Color.WHITE);
        setupBackRank(squares, 8, Color.BLACK);
        setupPawns(squares, 7, Color.BLACK);
    }

    private static void setupBackRank(Piece[][] squares, int rank, Color color) {
        squares[rank][0] = new Piece(PieceType.ROOK, color);
        squares[rank][1] = new Piece(PieceType.KNIGHT, color);
        squares[rank][2] = new Piece(PieceType.BISHOP, color);
        squares[rank][3] = new Piece(PieceType.QUEEN, color);
        squares[rank][4] = new Piece(PieceType.KING, color);
        squares[rank][5] = new Piece(PieceType.BISHOP, color);
        squares[rank][6] = new Piece(PieceType.KNIGHT, color);
        squares[rank][7] = new Piece(PieceType.ROOK, color);
    }

    private static void setupPawns(Piece[][] squares, int rank, Color color) {
        for (int file = 0; file < 8; file++) {
            squares[rank][file] = new Piece(PieceType.PAWN, color);
        }
    }

}
