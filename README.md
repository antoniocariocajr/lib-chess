# ♟️ Lib Chess

> **Uma biblioteca Java robusta e moderna para lógica de xadrez.**

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

**Lib Chess** é um pacote Java projetado para lidar com toda a complexidade das regras do xadrez. Construído com princípios de **Domain-Driven Design (DDD)** e **Imutabilidade Total**, ele oferece uma base sólida para criar jogos de xadrez, motores de análise ou aplicações educacionais.

---

## 🚀 Funcionalidades

- **Regras Completas**: Validação rigorosa de movimentos, incluindo **Roque**, **En Passant** e **Promoção**.
- **Motor de IA (Minimax)**: Motor integrado com busca Minimax e **Poda Alpha-Beta** para tomada de decisão.
- **Suporte a PGN**: Importe e exporte partidas completas usando o formato padrão **Portable Game Notation**.
- **Gerenciamento de Estado (Undo/Redo)**: Sistema integrado para desfazer e refazer movimentos com segurança.
- **Estado Imutável**: O estado do jogo e do tabuleiro são completamente imutáveis, garantindo thread-safety e facilitando o desenvolvimento de interfaces reativas.
- **Suporte a FEN & UCI**: Compatibilidade total com Forsyth-Edwards Notation e Universal Chess Interface.
- **Validação Perft**: Geração de movimentos validada exaustivamente contra benchmarks mundiais para garantir 100% de correção.

---

## 🛠️ Como Usar

### 1. Iniciar e Movimentar (Com Undo/Redo)

Use o `MatchManager` para orquestrar o estado da partida e ter controle total do histórico.

```java
import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.manager.MatchManager;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.factory.MoveFactory;

// Iniciar gerenciador
MatchManager manager = new MatchManager(ChessFactory.create());

// Aplicar um movimento (UCI)
Move move = MoveFactory.fromUci("e2e4", manager.getCurrentMatch().board().pieceAt("e2"));
manager.applyMove(move);

// Desfazer o último lance
manager.undo();

// Refazer o lance
manager.redo();
```

### 2. Exportar para PGN

Gere o registro completo da partida para salvar ou compartilhar.

```java
import com.bill.chess.domain.factory.PgnExporter;

String pgn = PgnExporter.export(manager.getCurrentMatch());
System.out.println(pgn);
// Saída: 1. e4 e5 2. Nf3 ...
```

### 3. Usar a Inteligência Artificial

Encontre a melhor jogada para qualquer posição.

```java
import com.bill.chess.domain.rule.ai.MiniMaxEngine;
import com.bill.chess.domain.enums.Color;

MiniMaxEngine engine = new MiniMaxEngine(3); // Profundidade 3
Move bestMove = engine.findBestMove(match.board(), Color.WHITE);
```

---

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura baseada em domínio e princípios funcionais:

```text
com.bill.chess
├── domain
│   ├── model       # Records imutáveis (Board, Piece, ChessMatch)
│   ├── manager     # Orquestradores de estado (MatchManager - Undo/Redo)
│   ├── rule        # Lógica de negócio e IA (MiniMax, Evaluation)
│   ├── generator   # Motores de geração de movimentos (Pawn, Sliding, etc.)
│   └── factory     # Conversores e fábricas (PgnLoader, FEN, Position)
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Este projeto utiliza **Perft Tests** para garantir que nenhuma alteração quebre as regras fundamentais do xadrez.

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT.
