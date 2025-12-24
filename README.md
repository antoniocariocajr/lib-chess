# ♟️ Lib Chess

> **Uma biblioteca Java robusta e moderna para lógica de xadrez.**

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Build Status](https://github.com/antoniocariocajr/lib-chess/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/antoniocariocajr/lib-chess/actions)

**Lib Chess** é um pacote Java projetado para lidar com toda a complexidade das regras do xadrez. Construído com princípios de **Domain-Driven Design (DDD)** e **Imutabilidade**, ele oferece uma base sólida para criar jogos de xadrez, motores de análise ou aplicações educacionais.

---

## 🚀 Funcionalidades

- **Regras Completas**: Validação de movimentos, incluindo **Roque**, **En Passant** e **Promoção de Peão**.
- **Estado Imutável**: O estado do jogo é gerenciado através de _Records_, garantindo thread-safety e previsibilidade.
- **Suporte a FEN**: Importe e exporte estados de jogo usando a notação padrão Forsyth-Edwards Notation.
- **Suporte a UCI**: Crie movimentos usando a notação Universal Chess Interface (ex: "e2e4").
- **Detecção de Xeque**: Calculadoras integradas para verificar se o Rei está em xeque ou xeque-mate.
- **Arquitetura Limpa**: Separação clara entre domínio (`domain`) e infraestrutura, facilitando a manutenção e testes.

---

## 📦 Instalação

Adicione a dependência ao seu `pom.xml`:

```xml
<dependency>
    <groupId>com.github.antoniocariocajr</groupId>
    <artifactId>lib-chess</artifactId>
    <version>0.1.0</version>
</dependency>
```

---

## 🛠️ Como Usar

### 1. Iniciar uma Nova Partida

Use a `ChessFactory` para criar o estado inicial padrão do tabuleiro.

```java
import com.bill.chess.domain.factory.ChessFactory;
import com.bill.chess.domain.model.ChessMatch;

ChessMatch match = ChessFactory.create();
System.out.println("Partida iniciada: " + match.status());
```

### 2. Executar um Movimento

A execução de movimentos é funcional: você passa o estado atual e o movimento, e recebe um **novo** estado.

```java
import com.bill.chess.domain.factory.MoveFactory;
import com.bill.chess.domain.factory.PositionFactory;
import com.bill.chess.domain.move.MoveApplicator;
import com.bill.chess.domain.model.Move;
import com.bill.chess.domain.model.Piece;
import com.bill.chess.domain.model.Position;

// 1. Identificar as posições
Position from = PositionFactory.fromNotation("e2");
        Position to = PositionFactory.fromNotation("e4");

        // 2. Obter a peça do tabuleiro atual
        Piece piece = match.board().pieceAt(from).orElseThrow();

        // 3. Criar o objeto de movimento (ex: via UCI ou Factory)
        Move move = MoveFactory.fromUci("e2e4", piece);

        // 4. Executar e obter o novo estado da partida
        ChessMatch nextMatch = MoveApplicator.executeMove(match, move);
```

### 3. Usar FEN (Forsyth-Edwards Notation)

Carregue cenários específicos ou salve o progresso.

```java
// Exportar para FEN
String currentFen = ChessFactory.toFen(nextMatch);
System.out.println(currentFen); 
// Saída ex: rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 1

// Importar de FEN
ChessMatch scenario = ChessFactory.fromFen("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 1");
```

---

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura baseada em domínio:

```text
com.bill.chess
├── domain
│   ├── model       # Entidades principais (Board, Piece, ChessMatch)
│   ├── rule        # Regras de negócio (Xeque, Movimentos Legais)
│   ├── factory     # Criação de objetos complexos e FEN/UCI
│   ├── executor    # Motor de execução de movimentos (MoveExecutor)
│   └── enums       # Constantes (Color, PieceType)
└── infra
    └── validation  # Validações técnicas e exceções
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir _issues_ ou enviar _pull requests_ com melhorias, correções de bugs ou novas funcionalidades.

1. Faça um Fork do projeto
2. Crie sua Feature Branch (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a Branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
