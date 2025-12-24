package com.bill.chess.domain.move;

import com.bill.chess.domain.exception.ChessEngineException;
import com.bill.chess.domain.exception.InvalidMoveException;
import com.bill.chess.domain.factory.PieceFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

import java.util.Optional;

import static com.bill.chess.domain.validation.MoveValidation.*;

public final class MoveEnricher {

    private MoveEnricher(){}

    public static Move fromUci(Board board, String uci) {

        validateUci(uci);
        Position from = PositionFactory.fromNotation(uci.substring(0,2));
        Position to   = PositionFactory.fromNotation(uci.substring(2,4));

        validatePositions(from,to);

        Piece moved = board.pieceAt(from)
                .orElseThrow(() -> new InvalidMoveException("No piece at " + from));

        // promotion
        Piece promoted = null;
        if (uci.length() == 5) {
            promoted = PieceFactory.toPiece(uci.charAt(4));
        }

        // captured
        Optional<Piece> captured = board.pieceAt(to);

        // en-passant
        boolean isEnPassant = moved.isPawn()
                && to.rank()== from.rank()+2
                && captured.isEmpty();

        // roque
        boolean isCastling = moved.isKing() && Math.abs(to.file() - from.file()) == 2;

        return new Move(from, to,
                captured.orElse(null),
                promoted,
                moved,
                isCastling,
                isEnPassant);
    }

    public static Move fromMove(Board board, Move move){
        validatePositions(move.from(),move.to());
        // piece moved
        Piece moved = board.pieceAt(move.from())
                .orElseThrow(() -> new InvalidMoveException("No piece at " + move.from()));
        // promotion
        if(moved.isPawn()&&(move.to().rank()==8||move.to().rank()==1)){
            if(move.promotion().isEmpty())
                throw new ChessEngineException("Not found promotion pawn");
        }
        // captured
        Optional<Piece> captured = board.pieceAt(move.to());
        // en-passant
        boolean isEnPassant = moved.isPawn()
                && move.to().rank()== move.from().rank()+2
                && captured.isEmpty();

        // roque
        boolean isCastling = moved.isKing() && Math.abs(move.to().file() - move.from().file()) == 2;

        return new Move(move.from(), move.to(),
                captured.orElse(null),
                move.pawnPromotion(),
                moved,
                isCastling,
                isEnPassant);
    }
}
