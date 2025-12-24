package com.bill.chess.service;

import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.move.MoveApplicator;
import com.bill.chess.domain.move.MoveEnricher;
import com.bill.chess.domain.move.MoveValidator;

import java.util.List;

import static com.bill.chess.domain.rule.LegalMoveFilter.forColor;
import static com.bill.chess.domain.rule.PieceMoveProvider.forPiece;
import static com.bill.chess.domain.validation.ChessValidation.validateFen;

public final class MatchService {
    private MatchService(){}

    public static ChessMatch executeMove(ChessMatch match, Move move) {
        Move enriched = MoveEnricher.fromMove(match.board(), move);
        MoveValidator.validate(match, enriched);
        return MoveApplicator.apply(match, enriched);
    }

    public static List<Move> legalMove(String fen){
        validateFen(fen);
        ChessMatch chessMatch = ChessFactory.fromFen(fen);
        return forColor(chessMatch);
    }

    public static List<Move> movesByPosition(String fen, String notation){
        validateFen(fen);
        ChessMatch chessMatch = ChessFactory.fromFen(fen);
        Position from = PositionFactory.fromNotation(notation);
        return  forPiece(chessMatch,from);
    }
}
