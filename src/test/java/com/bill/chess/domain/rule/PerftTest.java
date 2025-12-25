package com.bill.chess.domain.rule;

import com.bill.chess.domain.converter.FenConverter;
import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.model.ChessMatch;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.move.MoveApplicator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes Perft (Performance Test) para validar a correção da geração de
 * movimentos.
 * Compara o número de nós encontrados em profundidades específicas com valores
 * conhecidos.
 */
class PerftTest {

    @Test
    void initialPositionDepth1() {
        ChessMatch match = ChessFactory.create();
        assertEquals(20, perft(match, 1));
    }

    @Test
    void initialPositionDepth2() {
        ChessMatch match = ChessFactory.create();
        assertEquals(400, perft(match, 2));
    }

    @Test
    void initialPositionDepth3() {
        ChessMatch match = ChessFactory.create();
        assertEquals(8902, perft(match, 3));
    }

    @Test
    @Disabled("Demora alguns segundos")
    void initialPositionDepth4() {
        ChessMatch match = ChessFactory.create();
        assertEquals(197281, perft(match, 4));
    }

    /**
     * Posição clássica para testar regras complexas (Roque, En Passant, Promoção).
     * "Kiwipete"
     */
    @Test
    void kiwipeteDepth1() {
        ChessMatch match = FenConverter.fromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertEquals(48, perft(match, 1));
    }

    @Test
    void kiwipeteDepth2() {
        ChessMatch match = FenConverter.fromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertEquals(2039, perft(match, 2));
    }

    /**
     * Função recursiva de contagem de movimentos.
     */
    private long perft(ChessMatch match, int depth) {
        if (depth == 0)
            return 1;

        List<Move> legalMoves = LegalMoveFilter.forColor(match);
        if (depth == 1)
            return legalMoves.size();

        long nodes = 0;
        for (Move move : legalMoves) {
            ChessMatch nextMatch = MoveApplicator.apply(match, move);
            nodes += perft(nextMatch, depth - 1);
        }
        return nodes;
    }
}
