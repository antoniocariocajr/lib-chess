package com.bill.chess.domain.rule;

import com.bill.chess.domain.model.Move;

public final class HalfMoveClock {
    private HalfMoveClock(){}

    public static Integer updateHalfMove(Integer clock, Move move){
        if(move.captured().isPresent()||move.pieceMoved().isPawn())return 0;
        return clock+1;
    }
}
