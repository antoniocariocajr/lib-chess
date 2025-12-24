package com.bill.chess.domain.rule;

import com.bill.chess.domain.enums.Color;

public final class FullMoveNumber {

    private FullMoveNumber(){}

    public static Integer updateFullMove(Integer fullMoveNumber, Color color){
        return color.isWhite() ?
                fullMoveNumber+1 :
                fullMoveNumber;
    }
}
