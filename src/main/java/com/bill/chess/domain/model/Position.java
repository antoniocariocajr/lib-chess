package com.bill.chess.domain.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Position(@Positive int rank, @PositiveOrZero int file) {

}
