# Guia da Inteligência Artificial (AI Guide)

A **Lib Chess** vem equipada com um motor de busca clássico baseado no algoritmo Minimax. Este guia explica como ele toma decisões.

## 🧠 O Algoritmo Minimax

O `MiniMaxEngine` busca a melhor jogada simulando ramificações da partida. Em cada nível da árvore de busca, o motor assume que:

- O **Branco** quer maximizar a pontuação.
- O **Preto** quer minimizar a pontuação.

### Otimizações Implementadas

Para lidar com a explosão exponencial de movimentos possíveis, utilizamos três técnicas principais:

1. **Poda Alpha-Beta (Alpha-Beta Pruning)**: Ignora ramos da árvore que comprovadamente não afetarão a decisão final, economizando tempo de processamento.
2. **Ordenação de Movimentos (Move Sorting)**: Prioriza a análise de capturas e xeque-mates. Quanto antes encontrarmos um movimento "bom", mais ramos podemos podar.
3. **Quiescence Search**: Evita o "efeito horizonte". Quando a busca atinge a profundidade máxima, ela continua analisando capturas até que a posição se torne "estável" (quiescent).

---

## 📊 Função de Avaliação

Como a IA decide se uma posição é boa? O `EvaluationFunction` analisa o tabuleiro e atribui uma pontuação numérica:

- **Material**: Cada peça tem um valor (Peão=100, Cavalo=320, Bispo=330, Torre=500, Rainha=900, Rei=20000).
- **Posicionamento (PST - Piece Square Tables)**: Peças em casas centrais operam melhor que em casas laterais.
- **Segurança do Rei**: Bônus/Penalidades baseados na exposição do rei.

---

## ⚙️ Exemplo de Configuração

Você pode ajustar a "força" da IA alterando a profundidade da busca:

```java
// Profundidade 3: Rápido e razoável para jogadores casuais
MiniMaxEngine engine = new MiniMaxEngine(3);

// Profundidade 5: Mais lento, mas consideravelmente mais forte
// Recomenda-se rodar em threads separadas para não travar a UI
Move best = engine.findBestMove(board, Color.WHITE);
```

> [!WARNING]
> Devido à natureza CPU-Intensive da busca, profundidades acima de 6 podem levar vários segundos ou minutos dependendo do hardware, já que o motor atual é single-threaded.
