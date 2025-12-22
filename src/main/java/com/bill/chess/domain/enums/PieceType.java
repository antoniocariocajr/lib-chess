package com.bill.chess.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PieceType {
    PAWN('P'),
    ROOK('R'),
    KNIGHT('N'),
    BISHOP('B'),
    QUEEN('Q'),
    KING('K');

    private final char typeSymbol;
    private static final Map<Character, PieceType> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(pt -> pt.typeSymbol, pt -> pt));

    PieceType(char typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public char symbol() {
        return typeSymbol;
    }

    public static PieceType fromSymbol(char ch) {
        return LOOKUP.get(Character.toUpperCase(ch));
    }
}
