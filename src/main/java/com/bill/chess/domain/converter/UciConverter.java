package com.bill.chess.domain.converter;

import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PieceFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.validation.FenValidation;
import com.bill.chess.domain.validation.MoveValidation;
import com.bill.chess.domain.validation.PieceValidation;
import com.bill.chess.domain.validation.UciValidation;

import java.util.Optional;

public final class UciConverter {
    private UciConverter() {
    }

    public static Move fromUci(String uci, String fen) {

        FenValidation.validateFen(fen);
        UciValidation.validateUci(uci);

        Position from = PositionFactory.fromNotation(uci.substring(0, 2));
        Position to = PositionFactory.fromNotation(uci.substring(2, 4));
        Boolean isEnpassante = false;
        Boolean isCastling = false;
        Piece promoted = null;
        if (uci.length() == 5) {
            promoted = PieceFactory.toPiece(uci.substring(4));
        }

        ChessMatch match = FenConverter.fromFen(fen);
        Board board = match.board();
        Optional<Piece> potentialPieceMoved = board.pieceAt(from);
        PieceValidation.validatePiece(potentialPieceMoved);
        Piece pieceMoved = potentialPieceMoved.get();
        Optional<Piece> potentialOccupied = board.pieceAt(to);
        if (potentialOccupied.isPresent()) {
            MoveValidation.validateCapture(potentialOccupied.get(), pieceMoved);
        }
        if (pieceMoved.isKing() && Math.abs(from.file() - to.file()) == 2) {
            isCastling = true;
        }
        if (pieceMoved.isPawn() && Math.abs(from.rank() - to.rank()) == 2) {
            isEnpassante = true;
        }
        return MoveFactory.of(from, to, potentialOccupied.orElse(null), promoted, pieceMoved, isCastling, isEnpassante);
    }

    public static String toUci(Move move) {
        StringBuilder sb = new StringBuilder();
        sb.append(PositionFactory.toNotation(move.from())).append(PositionFactory.toNotation(move.to()));
        move.promotion().ifPresent(p -> sb.append(PieceFactory.toUnicode(p)));
        return sb.toString();
    }
}
