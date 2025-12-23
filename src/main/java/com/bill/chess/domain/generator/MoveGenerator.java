package com.bill.chess.domain.generator;

import java.util.List;

import com.bill.chess.domain.model.Board;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

public interface MoveGenerator {
    List<Move> generate(Board board, Position from, Piece piece, Position enPassant);
}
