package com.bill.chess.domain.rule;

import com.bill.chess.domain.enums.CastleRight;
import com.bill.chess.domain.enums.Color;
import com.bill.chess.domain.enums.MatchStatus;
import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Position;

import java.util.List;
import java.util.Set;

import static com.bill.chess.domain.rule.InCheckCalculator.isInCheck;
import static com.bill.chess.domain.rule.LegalMoveFilter.forColor;

public final class StatusMatchCalculator {

    private StatusMatchCalculator(){}

    public static MatchStatus calculatorStatus(ChessMatch match){
        List<Move> moves = forColor(match);
        if(moves.isEmpty()) {
            if (isInCheck(match.board(), match.currentColor())) {
                return match.currentColor().isWhite() ?
                        MatchStatus.BLACK_WINS :
                        MatchStatus.WHITE_WINS;
            }
            return MatchStatus.STALEMATE;
        }
        if (match.halfMoveClock()==100)return MatchStatus.DRAW;
        return MatchStatus.IN_PROGRESS;
    }
    public static MatchStatus calculatorStatus(Board board, Color color,
                                               Set<CastleRight> rights,
                                               Position enPassant,
                                               Integer halfMoveClock){
        List<Move> moves = forColor(board, color, rights, enPassant);
        if(moves.isEmpty()) {
            if (isInCheck(board, color)) {
                return color.isWhite() ?
                        MatchStatus.BLACK_WINS :
                        MatchStatus.WHITE_WINS;
            }
            return MatchStatus.STALEMATE;
        }
        if (halfMoveClock==100)return MatchStatus.DRAW;
        return MatchStatus.IN_PROGRESS;
    }
}
