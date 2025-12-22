package com.bill.chess.domain.model;

import java.util.Objects;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Position(@Positive int rank, @PositiveOrZero int file) {

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        Position position = (Position) object;
        return rank == position.rank && file == position.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }

}
