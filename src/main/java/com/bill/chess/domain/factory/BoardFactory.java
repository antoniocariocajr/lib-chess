package com.bill.chess.domain.factory;

import java.util.ArrayList;
import java.util.List;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.PieceType;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.validation.BoardValidation;

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
