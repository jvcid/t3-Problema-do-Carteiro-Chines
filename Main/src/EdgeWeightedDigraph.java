import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EdgeWeightedDigraph {

    private final int  V;          // número de vértices
    private int        E;          // número de arestas
    private final List<DirectedEdge>[] adj;  // adj[v] = arestas saindo de v
    private final int[] indegree;   // grau de entrada por vértice

    @SuppressWarnings("unchecked")
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Vértices inválidos: " + V);
        this.V        = V;
        this.indegree = new int[V];
        this.adj      = new ArrayList[V];
        for (int v = 0; v < V; v++) adj[v] = new ArrayList<>();
    }

    public int  V() { return V; }
    public int  E() { return E; }

    /** Adiciona uma aresta dirigida ponderada ao dígrafo. */
    public void addEdge(DirectedEdge edge) {
        int v = edge.from(), w = edge.to();
        validateVertex(v); validateVertex(w);
        adj[v].add(edge);
        indegree[w]++;
        E++;
    }

    public List<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int outdegree(int v) { validateVertex(v); return adj[v].size(); }
    public int indegree(int v)  { validateVertex(v); return indegree[v]; }

    public List<DirectedEdge> edges() {
        List<DirectedEdge> list = new ArrayList<>();
        for (int v = 0; v < V; v++) list.addAll(adj[v]);
        return Collections.unmodifiableList(list);
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("Vértice " + v + " inválido.");
    }
}