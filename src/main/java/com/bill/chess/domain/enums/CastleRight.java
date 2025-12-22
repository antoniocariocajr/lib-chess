package com.bill.chess.domain.enums;

public enum CastleRight {
    WHITE_KINGSIDE('K'), // Roque pequeno branco
    WHITE_QUEENSIDE('Q'), // Roque grande branco
    BLACK_KINGSIDE('k'), // Roque pequeno preto
    BLACK_QUEENSIDE('q'); // Roque grande preto

    private final char fenSymbol;

    CastleRight(char fenSymbol) {
        this.fenSymbol = fenSymbol;
    }

    public char fenSymbol() {
        return fenSymbol;
    }
}
