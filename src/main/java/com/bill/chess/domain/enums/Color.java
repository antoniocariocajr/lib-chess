package com.bill.chess.domain.enums;

public enum Color {
    WHITE('w'),
    BLACK('b');

    private final char symbol;

    Color(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public boolean isWhite() {
        return this == WHITE;
    }
}
