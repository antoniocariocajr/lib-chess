package com.bill.chess.service.mapper;

import com.bill.chess.domain.enums.Color;
import com.bill.chess.infra.exception.InvalidColorException;

public final class ColorMapper {

    public static Color toColor(char symbol) {
        return switch (Character.toLowerCase(symbol)) {
            case 'w' -> Color.WHITE;
            case 'b' -> Color.BLACK;
            default -> throw new InvalidColorException("Invalid color symbol: " + symbol);
        };
    }

    public static Color toColor(String symbol) {
        return switch (symbol.toLowerCase()) {
            case "w" -> Color.WHITE;
            case "b" -> Color.BLACK;
            case "white" -> Color.WHITE;
            case "black" -> Color.BLACK;
            default -> throw new InvalidColorException("Invalid color symbol: " + symbol);
        };
    }

    public static char toSymbol(Color color) {
        return color.symbol();
    }

}
