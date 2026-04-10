import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class DirectedEulerianCycle {

    private List<Integer> cycle;   // sequência de vértices (null se sem circuito)
    private double           totalCost;

    public DirectedEulerianCycle(EdgeWeightedDigraph G) {
        cycle     = null;
        totalCost = 0.0;

        // ── 1. Verificação de pré-condição ────────────────────────────────
        if (G.E() == 0) return;
        for (int v = 0; v < G.V(); v++)
            if (G.indegree(v) != G.outdegree(v)) return;

        // ── 2. Array de índices de adjacência ─────────────────────────────
        int[] adjIdx = new int[G.V()];       // próxima aresta a consumir
        @SuppressWarnings("unchecked")
        List<DirectedEdge>[] adjLists = new List[G.V()];
        for (int v = 0; v < G.V(); v++)
            adjLists[v] = new ArrayList<>(G.adj(v));

        // ── 3. Vértice inicial ────────────────────────────────────────────
        int s = firstNonIsolated(G);
        if (s == -1) return;

        // ── 4. Loop principal de Hierholzer ───────────────────────────────
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer>  path  = new ArrayList<>();

        stack.push(s);

        while (!stack.isEmpty()) {
            int v = stack.peek();

            if (adjIdx[v] < adjLists[v].size()) {
                // topo com aresta disponível → avança
                DirectedEdge edge = adjLists[v].get(adjIdx[v]++);
                stack.push(edge.to());
            } else {
                // topo esgotado → registra no caminho
                path.add(stack.pop());
            }
        }

        // ── 5. Reverte e valida ───────────────────────────────────────────
        Collections.reverse(path);

        if (path.size() == G.E() + 1
                && path.get(0).equals(path.get(path.size() - 1))) {
            cycle = Collections.unmodifiableList(path);
            for (DirectedEdge e : G.edges()) totalCost += e.weight();
        }
    }

    /** true se existe circuito euleriano. */
    public boolean hasCycle()    { return cycle != null; }

    /** Sequência de vértices do circuito (primeiro == último), ou null. */
    public List<Integer> cycle() { return cycle; }

    /** Custo total (soma dos pesos de todas as arestas). */
    public double totalCost()   { return totalCost; }

    private static int firstNonIsolated(EdgeWeightedDigraph G) {
        for (int v = 0; v < G.V(); v++) if (G.outdegree(v) > 0) return v;
        return -1;
    }
}