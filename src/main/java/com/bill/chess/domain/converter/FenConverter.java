package com.bill.chess.domain.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.factory.BoardFactory;
import com.bill.chess.domain.factory.ColorFactory;
import com.bill.chess.domain.factory.PieceFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.validation.BoardValidation;
import com.bill.chess.domain.validation.FenValidation;

import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;
import static com.bill.chess.domain.rule.StatusMatchCalculator.calculatorStatus;

/**
 * Handles FEN (Forsyth-Edwards Notation) conversions for chess matches and
 * boards.
 */
public final class FenConverter {

    private FenConverter() {
    }

    public static ChessMatch fromFen(String fen) {
        FenValidation.validateFen(fen);
        String[] fenParts = fen.split(" ");

        Board board = boardFromFen(fenParts[0], new ArrayList<>());
        Color color = ColorFactory.toColor(fenParts[1]);
        Set<CastleRight> castleRights = castleFromFen(fenParts[2]);
        Position enPassantSquare = PositionFactory.fromFen(fenParts[3]);
        Integer halfMoveClock = Integer.parseInt(fenParts[4]);
        Integer fullMoveNumber = Integer.parseInt(fenParts[5]);

        boolean inCheck = isInCheck(board, color);
        MatchStatus status = calculatorStatus(board, color, castleRights, enPassantSquare, halfMoveClock);

        return new ChessMatch(board, status, color, castleRights, enPassantSquare, halfMoveClock,
                fullMoveNumber, inCheck);
    }

    public static String toFen(ChessMatch match) {
        StringBuilder fen = new StringBuilder();
        fen.append(boardToFen(match.board()));
        fen.append(" ");
        fen.append(ColorFactory.toSymbol(match.currentColor()));
        fen.append(" ");
        fen.append(castleToFen(match.castleRights()));
        fen.append(" ");
        if (match.enPassant().isPresent())
            fen.append(PositionFactory.toNotation(match.enPassant().get()));
        else
            fen.append("-");
        fen.append(" ");
        fen.append(match.halfMoveClock());
        fen.append(" ");
        fen.append(match.fullMoveNumber());
        return fen.toString();
    }

    public static Board boardFromFen(String fenBoard, List<Move> history) {
        FenValidation.validateFenBoard(fenBoard);

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
        BoardValidation.validateSquares(squares);

        return BoardFactory.of(squares, history);
    }

    public static String boardToFen(Board board) {
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

    private static Set<CastleRight> castleFromFen(String fenCastle) {
        Set<CastleRight> castleRights = new HashSet<>();
        if (fenCastle.equals("-"))
            return castleRights;
        for (char c : fenCastle.toCharArray()) {
            switch (c) {
                case 'K' -> castleRights.add(CastleRight.WHITE_KINGSIDE);
                case 'Q' -> castleRights.add(CastleRight.WHITE_QUEENSIDE);
                case 'k' -> castleRights.add(CastleRight.BLACK_KINGSIDE);
                case 'q' -> castleRights.add(CastleRight.BLACK_QUEENSIDE);
            }
        }
        return castleRights;
    }

    private static String castleToFen(Set<CastleRight> castleRights) {
        if (castleRights.isEmpty())
            return "-";
        StringBuilder fen = new StringBuilder();
        // Standard FEN order: KQkq
        if (castleRights.contains(CastleRight.WHITE_KINGSIDE))
            fen.append('K');
        if (castleRights.contains(CastleRight.WHITE_QUEENSIDE))
            fen.append('Q');
        if (castleRights.contains(CastleRight.BLACK_KINGSIDE))
            fen.append('k');
        if (castleRights.contains(CastleRight.BLACK_QUEENSIDE))
            fen.append('q');
        return fen.toString();
    }
}
