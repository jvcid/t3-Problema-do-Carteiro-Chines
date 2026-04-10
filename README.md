# T3 — Problema do Carteiro Chinês em Dígrafos

Implementação do **Método de Hierholzer** para encontrar um circuito euleriano
em um dígrafo ponderado já eulerizado, no contexto do Problema do Carteiro Chinês.
Linguagem: **Java** (referência: algs4-kw).

---

## 1. Estrutura do projeto

```
t3-pcc/
├── README.md
├── dados/
│   ├── entrada_original.txt       ← dígrafo de exemplo original (desbalanceado)
│   └── entrada_eulerizada.txt     ← dígrafo de exemplo após eulerização manual
└── src/
    ├── Main.java                  ← ponto de entrada
    ├── DirectedEdge.java          ← aresta dirigida ponderada
    ├── EdgeWeightedDigraph.java   ← dígrafo ponderado (listas de adjacência)
    └── DirectedEulerianCycle.java ← método de Hierholzer
```

---

## 2. Formato de entrada

```
V          ← número de vértices (inteiros 0..V-1)
E          ← número de arestas
v w peso   ← aresta dirigida v → w com o peso indicado
...
```

Linhas iniciadas por `#` são ignoradas (comentários).  
Arestas **repetidas** são permitidas em `entrada_eulerizada.txt`.

---

## 3. Como compilar e executar

### Pré-requisito

JDK 11 ou superior (`javac` e `java` disponíveis no PATH).

### Compilar (a partir da raiz do projeto)

```bash
javac -d out src/*.java
```

### Executar com o arquivo de exemplo

```bash
java -cp out Main dados/entrada_eulerizada.txt
```

### Executar com qualquer arquivo

```bash
java -cp out Main <caminho/para/arquivo.txt>
```

### Saída esperada

```
============================================
  PROBLEMA DO CARTEIRO CHINÊS — DÍGRAFO
  Método de Hierholzer
============================================

Arquivo: dados/entrada_eulerizada.txt

Vértices : 5
Arestas  : 10

╔══════════════════════════════════════════╗
║         GRAUS DE ENTRADA E SAÍDA         ║
╠══════╦═══════════╦══════════╦════════════╣
║  v   ║  entrada  ║   saída  ║  situação  ║
╠══════╬═══════════╬══════════╬════════════╣
║  0   ║     2     ║    2    ║  ✔ OK        ║
║  1   ║     2     ║    2    ║  ✔ OK        ║
║  2   ║     1     ║    1    ║  ✔ OK        ║
║  3   ║     2     ║    2    ║  ✔ OK        ║
║  4   ║     3     ║    3    ║  ✔ OK        ║
╚══════╩═══════════╩══════════╩════════════╝

► Grafo BALANCEADO — todos os vértices satisfazem grau_in == grau_out.

► Executando o Método de Hierholzer...

Sequência de vértices:
  0 -> 1 -> 4 -> 2 -> 0 -> 4 -> 3 -> 1 -> 4 -> 3 -> 0

Arestas percorridas (em ordem):
  0 -> 1
  ...

────────────────────────────────────────────
  Custo total do circuito: 10
────────────────────────────────────────────
```

---

## 4. Grafo de exemplo — análise de eulerização

Mapeamento: **a=0, b=1, c=2, d=3, e=4**

### Graus no grafo original

| Vértice | grau_in | grau_out | Situação        |
|---------|---------|----------|-----------------|
| a (0)   | 2       | 2        | ✔ OK            |
| b (1)   | 2       | 1        | ✘ DESBALANCEADO |
| c (2)   | 1       | 1        | ✔ OK            |
| d (3)   | 1       | 2        | ✘ DESBALANCEADO |
| e (4)   | 2       | 2        | ✔ OK            |

### Vértices desbalanceados

- **b**: `grau_out − grau_in = −1` → falta 1 aresta de saída
- **d**: `grau_out − grau_in = +1` → falta 1 aresta de entrada

### Eulerização — arestas inseridas

O caminho de menor custo de b até d é `b → e → d` (custo = 2).

| Aresta inserida | Peso | Efeito                          |
|-----------------|------|---------------------------------|
| b → e           | 1    | +1 saída em b → b balanceado ✔  |
| e → d           | 1    | +1 entrada em d → d balanceado ✔|

O vértice e recebe +1 entrada e +1 saída: permanece balanceado.

**Custo total = 8 (original) + 2 (inseridos) = 10**

### Circuito euleriano

```
0 → 1 → 4 → 2 → 0 → 4 → 3 → 1 → 4 → 3 → 0
(a → b → e → c → a → e → d → b → e → d → a)
```

---

## 5. Implementação — Método de Hierholzer

```
1. Verificar grau_in == grau_out para todo vértice.
2. Escolher vértice inicial s (primeiro não isolado).
3. Loop com pilha e índices de adjacência por vértice:
   - topo com arestas disponíveis → avança, empilha destino
   - topo esgotado → remove da pilha, adiciona ao caminho
4. Reverter o caminho coletado → circuito euleriano.
5. Custo = soma dos pesos de todas as arestas.
```

Complexidade: **O(E)** tempo e espaço.

---

## 6. Link do vídeo

> https://youtu.be/VYIy3nh6Nlo

---

## 7. Referências

- Sedgewick & Wayne — *Algorithms* (algs4-kw): https://algs4.cs.princeton.edu
- TUM — Directed Chinese Postman: https://algorithms.discrete.ma.tum.de/graph-algorithms/directed-chinese-postman/index_en.html
