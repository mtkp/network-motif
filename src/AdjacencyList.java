// AdjacencyList.java

public class AdjacencyList {

    private CompactHashSet nodes;

    public AdjacencyList() {
        this.nodes = new CompactHashSet();
    }

    private AdjacencyList(AdjacencyList adjacencyList) {
        this.nodes = adjacencyList.nodes.copy();
    }

    public void add(int node) {
        nodes.add(node);
    }

    public CompactHashSet.Iter iterator() {
        return nodes.iterator();
    }

    public boolean contains(int node) {
        return nodes.contains(node);
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public String toString() {
        return nodes.toString();
    }

    public boolean remove(int node) {
        return nodes.remove(node);
    }

    public AdjacencyList copy() {
        return new AdjacencyList(this);
    }
}
