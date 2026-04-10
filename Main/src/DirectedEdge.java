public class DirectedEdge {

    private final int    v;       // vértice de origem
    private final int    w;       // vértice de destino
    private final double weight;  // peso da aresta

    /** Cria uma aresta dirigida de v para w com o peso informado. */
    public DirectedEdge(int v, int w, double weight) {
        if (v < 0) throw new IllegalArgumentException("Vértice de origem inválido: " + v);
        if (w < 0) throw new IllegalArgumentException("Vértice de destino inválido: " + w);
        this.v      = v;
        this.w      = w;
        this.weight = weight;
    }

    /** Retorna o vértice de origem. */
    public int from()   { return v; }

    /** Retorna o vértice de destino. */
    public int to()     { return w; }

    /** Retorna o peso da aresta. */
    public double weight() { return weight; }

    @Override
    public String toString() {
        return v + "->" + w + " (peso=" + weight + ")";
    }
}