# Conceitos Centrais (Core Concepts)

Esta página detalha as decisões de design e os pilares técnicos da **Lib Chess**.

## 💎 Imutabilidade Total

A biblioteca foi construída utilizando **Java Records** (introduzidos definitivamente no Java 16/17 e explorados aqui no Java 21).

### Por que imutabilidade?

1. **Thread-Safety**: Várias threads podem ler o estado do tabuleiro simultaneamente sem necessidade de locks.
2. **Histórico nativo**: Desfazer um movimento é tão simples quanto retornar a instância anterior do `ChessMatch`.
3. **Previsibilidade**: O estado do jogo nunca é alterado "por trás das cenas". Cada movimento gera um novo universo.

### Exemplo: ChessMatch

```java
public record ChessMatch(
        Board board,
        MatchStatus status,
        Color currentColor,
        Set<CastleRight> castleRights,
        Position enPassantSquare,
        // ...
) { }
```

---

## 🏗️ Ciclo de Vida de um Movimento

A geração de movimentos é dividida em etapas para garantir performance e correção:

1. **Pseudo-Legal Moves**: São movimentos que a peça pode fisicamente fazer baseada apenas no seu tipo (ex: um Bispo andando na diagonal), ignorando se o Rei ficará em xeque.
2. **Legal Move Filtering**: Filtra os pseudo-legais para garantir que o Rei da cor que está movendo não termine em xeque.
3. **Move Enrichment**: Adiciona metadados ao movimento, como se foi uma captura, promoção ou special move (Roque/En Passant).

---

## 🧪 Validação com Perft (Performance Test)

Como saber se a lógica do xadrez está 100% correta? Usamos o **Perft**.

O Perft é um teste de depuração que conta todos os nós (movimentos) até uma certa profundidade. Comparamos nossos resultados com valores padrão da comunidade de xadrez:

| Profundidade | Posição Inicial | Kiwipete (Complexa) |
| :--- | :--- | :--- |
| 1 | 20 | 48 |
| 2 | 400 | 2.039 |
| 3 | 8.902 | 97.862 |
| 4 | 197.281 | 4.085.603 |

> [!TIP]
> Você pode encontrar esses testes em `PerftTest.java`. Eles são a nossa garantia de que regras complexas como *En Passant* ou *Roque* estão funcionando perfeitamente.

---

## 🛠️ Arquitetura de Domínio (DDD)

A estrutura de pacotes reflete o domínio do xadrez:

- `domain.model`: As entidades fundamentais (`Board`, `Piece`, `Position`).
- `domain.generator`: Especialistas em gerar movimentos para cada tipo de peça.
- `domain.move`: Responsável por aplicar transformações no tabuleiro e validar leis do jogo.
- `domain.manager`: A camada de orquestração (onde vive o `MatchManager`).
