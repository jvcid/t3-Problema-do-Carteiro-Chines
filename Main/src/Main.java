/**
 * Main.java
 * Ponto de entrada — Problema do Carteiro Chinês em Dígrafos.
 *
 * Uso:
 *   java Main                          → lê ../dados/entrada_eulerizada.txt
 *   java Main <caminho_para_arquivo>   → lê o arquivo informado
 *
 * Formato de entrada:
 *   V          ← número de vértices (inteiros 0..V-1)
 *   E          ← número de arestas
 *   v w peso   ← aresta dirigida v → w com o peso indicado
 *   ...
 *
 * Linhas iniciadas por '#' são tratadas como comentários e ignoradas.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // ── Leitura do arquivo de entrada ─────────────────────────────────────────

    private static EdgeWeightedDigraph lerGrafo(String caminho) throws IOException {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty() && !linha.startsWith("#")) {
                    linhas.add(linha);
                }
            }
        }

        int idx = 0;
        int V = Integer.parseInt(linhas.get(idx++));
        int E = Integer.parseInt(linhas.get(idx++));

        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int i = 0; i < E; i++) {
            String[] partes = linhas.get(idx++).split("\\s+");
            int    v    = Integer.parseInt(partes[0]);
            int    w    = Integer.parseInt(partes[1]);
            double peso = Double.parseDouble(partes[2]);
            G.addEdge(new DirectedEdge(v, w, peso));
        }
        return G;
    }

    // ── Exibição de graus ─────────────────────────────────────────────────────

    /**
     * Imprime os graus de entrada e saída de cada vértice.
     * Retorna true se o dígrafo está balanceado.
     */
    private static boolean exibirGraus(EdgeWeightedDigraph G) {
        System.out.println("--------------------------------------------");
        System.out.println("        GRAUS DE ENTRADA E SAÍDA         ");
        System.out.println("---------------------------------------------");
        System.out.println("  v   | entrada  |   saída  |  situação |");
        System.out.println("--------------------------------------------");

        boolean balanceado = true;
        for (int v = 0; v < G.V(); v++) {
            int entrada = G.indegree(v);
            int saida   = G.outdegree(v);
            boolean ok  = (entrada == saida);
            if (!ok) balanceado = false;
            String status = ok ? " OK        " : " DESBAL.   ";
            System.out.printf("|  %-3d |    %-5d |    %-5d|  %s|%n",
                    v, entrada, saida, status);
        }

        System.out.println("---------------------------------------------");
        return balanceado;
    }

    // ── Exibição do circuito ──────────────────────────────────────────────────

    private static void exibirCircuito(List<Integer> cycle, double totalCost) {
        System.out.println("\n---------------------------------------");
        System.out.println("         CIRCUITO EULERIANO             ");
        System.out.println("---------------------------------------");

        // Sequência de vértices
        System.out.print("Sequência de vértices:\n  ");
        for (int i = 0; i < cycle.size(); i++) {
            if (i > 0) System.out.print(" -> ");
            System.out.print(cycle.get(i));
        }
        System.out.println();

        // Arestas percorridas
        System.out.println("\nArestas percorridas (em ordem):");
        for (int i = 0; i < cycle.size() - 1; i++) {
            System.out.printf("  %d -> %d%n", cycle.get(i), cycle.get(i + 1));
        }

        System.out.println("\n" + "─".repeat(44));
        if (totalCost == (long) totalCost) {
            System.out.printf("  Custo total do circuito: %d%n", (long) totalCost);
        } else {
            System.out.printf("  Custo total do circuito: %.2f%n", totalCost);
        }
        System.out.println("─".repeat(44));
    }

    // ── Programa principal ────────────────────────────────────────────────────

    public static void main(String[] args) {

        // Determina o arquivo de entrada
        String caminho;
        if (args.length >= 1) {
            caminho = args[0];
        } else {
            // Caminho padrão relativo ao diretório de trabalho atual
            // Execute a partir da raiz do projeto: java -cp src Main
            caminho = Paths.get("dados", "entrada_eulerizada.txt").toString();
        }

        System.out.println("=".repeat(44));
        System.out.println("  PROBLEMA DO CARTEIRO CHINÊS — DÍGRAFO");
        System.out.println("  Método de Hierholzer");
        System.out.println("=".repeat(44));
        System.out.println("\nArquivo: " + caminho + "\n");

        // ── 1. Leitura do grafo ───────────────────────────────────────────────
        EdgeWeightedDigraph G;
        try {
            G = lerGrafo(caminho);
        } catch (IOException ex) {
            System.err.println("[ERRO] Não foi possível ler o arquivo: " + ex.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Vértices : " + G.V());
        System.out.println("Arestas  : " + G.E() + "\n");

        // ── 2. Graus de entrada e saída ───────────────────────────────────────
        boolean balanceado = exibirGraus(G);
        System.out.println();

        if (balanceado) {
            System.out.println("► Grafo BALANCEADO — todos os vértices satisfazem grau_in == grau_out.");
        } else {
            System.out.println("► Grafo NÃO BALANCEADO — existem vértices com grau_in ≠ grau_out.");
            System.out.println("  O dígrafo não admite circuito euleriano neste estado.");
            System.out.println("  Realize a eulerização manual e use entrada_eulerizada.txt.");
            return;
        }

        // ── 3. Método de Hierholzer ───────────────────────────────────────────
        System.out.println("\n► Executando o Método de Hierholzer...\n");
        DirectedEulerianCycle ec = new DirectedEulerianCycle(G);

        if (ec.hasCycle()) {
            exibirCircuito(ec.cycle(), ec.totalCost());
        } else {
            System.out.println("► O dígrafo não possui circuito euleriano.");
            System.out.println("  Verifique se o dígrafo é fortemente conexo.");
        }

        System.out.println();
    }
}