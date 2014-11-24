// Subgraph.java
//
// This class represents a single collection of graph nodes, referred to by
// node index values.
//

import java.io.UnsupportedEncodingException;

public class Subgraph {

    private int[] nodes;
    private AdjacencyMatrix matrix;
    private int currentSize;

    public Subgraph(int order) {
        // 'order' refers to the number of nodes the subgraph will contain
        this.currentSize = 0;
        this.nodes       = new int[order];
        this.matrix      = new AdjacencyMatrix(order);
    }

    public Subgraph copy() {
        Subgraph copy = new Subgraph(order());
        copy.currentSize = currentSize;
        for (int i = 0; i < size(); i++) {
            copy.nodes[i] = nodes[i];
        }
        copy.matrix = this.matrix.copy();
        return copy;
    }

    public int size() {
        return currentSize;
    }

    public int order() {
        return nodes.length;
    }

    public boolean isComplete() {
        return size() == order();
    }

    public int root() {
        return nodes[0];
    }

    public boolean contains(int node) {
        for (int i = 0; i < size(); i++) {
            if (get(i) == node) {
                return true;
            }
        }
        return false;
    }

    public void add(int node, AdjacencyList adjacencyList) {
        int index = node;
        nodes[currentSize] = index;

        for (int i = 0; i < currentSize; i++) {
            if (adjacencyList.contains(get(i))) {
                matrix.addEdge(i, currentSize);
            }
        }
        currentSize++;
    }

    public int get(int index) {
        return nodes[index];
    }

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < size(); i++) {
            s = s + get(i) + ", ";
        }
        s = s + "]";
        return s;
    }

    public String getByteString() {
        try {
            return new String(matrix.toBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to convert to graph6 format...");
            System.exit(-1);
            return null;
        }
    }
}
