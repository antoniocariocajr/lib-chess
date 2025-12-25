package com.bill.chess.domain.validation;

import com.bill.chess.domain.exception.InvalidUciException;

public final class UciValidation {
    private UciValidation() {
    }

    public static void validateUci(String uci) {
        if (uci == null || uci.length() < 4 || uci.length() > 5)
            throw new InvalidUciException("Invalid uci: " + uci);
    }
}
