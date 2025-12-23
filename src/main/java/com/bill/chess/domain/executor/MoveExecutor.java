package com.bill.chess.domain.executor;

import java.util.Optional;
import java.util.Set;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;
import com.bill.chess.domain.rule.MoveValidationRule;

public final class MoveExecutor {
    private MoveExecutor() {
    }

    public static ChessMatch executeMove(ChessMatch chessMatch, Move move) {
        Optional<Piece> pieceMoved = chessMatch.board().pieceAt(move.from());

        MoveValidationRule.validateTurn(pieceMoved.get(), chessMatch.currentColor());
        MoveValidationRule.validateMove(chessMatch.board(), move);
        Position enPassant = updateEnPassant(move);
        Color colorNewTurn = chessMatch.currentColor().opposite();
        Set<CastleRight> castleRights = updateCastlingRights(chessMatch.castleRights(), move);
        return new ChessMatch(chessMatch.board(), chessMatch.status(), colorNewTurn, castleRights, enPassant,
                chessMatch.halfMoveClock(), chessMatch.fullMoveNumber(), inCheck(chessMatch));
    }

    public static boolean inCheck(ChessMatch chessMatch) {
        return false;
    }

    public static Set<CastleRight> updateCastlingRights(Set<CastleRight> castleRights, Move move) {
        if (move.pieceMoved().isKing()) {
            castleRights.remove(move.pieceMoved().isWhite() ? CastleRight.WHITE_KINGSIDE : CastleRight.BLACK_KINGSIDE);
            castleRights
                    .remove(move.pieceMoved().isWhite() ? CastleRight.WHITE_QUEENSIDE : CastleRight.BLACK_QUEENSIDE);
            return castleRights;
        }
        if (move.pieceMoved().isRook()) {
            Position from = move.from();
            int rank = from.rank();
            if (from.file() == 7 && rank == (move.pieceMoved().isWhite() ? 1 : 8)) {
                castleRights
                        .remove(move.pieceMoved().isWhite() ? CastleRight.WHITE_KINGSIDE : CastleRight.BLACK_KINGSIDE);
            }
            if (from.file() == 0 && rank == (move.pieceMoved().isWhite() ? 1 : 8)) {
                castleRights.remove(
                        move.pieceMoved().isWhite() ? CastleRight.WHITE_QUEENSIDE : CastleRight.BLACK_QUEENSIDE);
            }
            return castleRights;
        }
        if (move.captured().isPresent() && move.captured().get().isRook()) {
            Position to = move.to();
            int rank = to.rank();
            if (to.file() == 7 && rank == (move.captured().get().isWhite() ? 1 : 8)) {
                castleRights.remove(
                        move.captured().get().isWhite() ? CastleRight.WHITE_KINGSIDE : CastleRight.BLACK_KINGSIDE);
            }
            if (to.file() == 0 && rank == (move.captured().get().isWhite() ? 1 : 8)) {
                castleRights.remove(
                        move.captured().get().isWhite() ? CastleRight.WHITE_QUEENSIDE : CastleRight.BLACK_QUEENSIDE);
            }
        }
        return castleRights;
    }

    public static Position updateEnPassant(Move move) {
        if (!move.pieceMoved().isPawn())
            return null;
        Position from = move.from();
        Position to = move.to();
        if (to.rank() != 3 && to.rank() != 6)
            return null;
        int dr = Math.abs(to.rank() - from.rank());
        if (dr == 2) {
            return PositionFactory.of(to.rank() == 3 ? 2 : 7, from.file());
        }
        return null;
    }

}
