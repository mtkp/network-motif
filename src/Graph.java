// Graph.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
        private List<AdjacencyList> adjacencyLists;

    public Graph(String filename) throws IOException {
        adjacencyLists = new ArrayList<AdjacencyList>();
        parse(filename);
    }

    // get the number of nodes in the graph
    public int size() {
        return adjacencyLists.size();
    }

    // get the adjacency list for a given node
    public AdjacencyList getAdjacencyList(Integer index) {
        return adjacencyLists.get(index);
    }

    // enumerate all subgraphs for a given node index
    public void enumerate(int index, int motifSize,
                          Map<String, Integer> subgraphs) {
        Subgraph subgraph = new Subgraph(motifSize);
        AdjacencyList adjacencyList = new AdjacencyList();
        CompactHashSet.Iter iter = getAdjacencyList(index).iterator();
        while (iter.hasNext()) {
            int next = iter.next();
            if (next > index) {
                adjacencyList.add(next);
            }
        }
        subgraph.add(index, getAdjacencyList(index));
        extend(subgraph, adjacencyList, subgraphs);
    }

    // extend the subgraphs recursively
    private void extend(Subgraph subgraph, AdjacencyList extension,
                        Map<String, Integer> subgraphs) {
        int v = subgraph.root();
        CompactHashSet.Iter wIter = extension.iterator();

        // optimize by not creating next extension if subgraph is
        // 1 node away from completion
        if (subgraph.size() == subgraph.order() - 1) {
            while (wIter.hasNext()) {
                int w = wIter.next();

                // construct a union of w and the existing subgraph
                Subgraph subgraphUnion = subgraph.copy();
                subgraphUnion.add(w, getAdjacencyList(w));

                // store in the subgraphs map using the subgraph byte string
                String repr = subgraphUnion.getByteString();
                int count = 1;
                if (subgraphs.containsKey(repr)) {
                    count += subgraphs.get(repr);
                }
                subgraphs.put(repr, count);
            }
        }

        while (wIter.hasNext()) {
            int w = wIter.next();
            wIter.remove();

            // next extension contains at least the current extension
            AdjacencyList nextExtension = extension.copy();

            // examine each node 'u' from the set of nodes adjacent to 'w'
            // and add it to the next extension if it is exclusive
            CompactHashSet.Iter uIter = getAdjacencyList(w).iterator();
            while (uIter.hasNext()) {
                int u = uIter.next();
                if (u > v) {
                    if (isExclusive(u, subgraph)) {
                        nextExtension.add(u);
                    }
                }
            }

            // construct a union of w and the existing subgraph
            Subgraph subgraphUnion = subgraph.copy();
            subgraphUnion.add(w, getAdjacencyList(w));

            extend(subgraphUnion, nextExtension, subgraphs);
        }
    }

    // returns true if the node index is exclusive to the given subgraph
    // (that is, is not already in the subgraph, and is not adjacent to any of
    //  the nodes in the subgraph)
    private boolean isExclusive(int node, Subgraph subgraph) {
        for (int i = 0; i < subgraph.size(); i++) {
            int subgraphNode = subgraph.get(i);
            if (subgraphNode == node) {
                return false;
            }
        }
        for (int i = 0; i < subgraph.size(); i++) {
            int subgraphNode = subgraph.get(i);
            if (getAdjacencyList(subgraphNode).contains(node)) {
                return false;
            }
        }
        return true;
    }

    // parses a data file into an adjacency list representing the graph
    private void parse(String filename) throws IOException {
        Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
        Map<Integer, String> indexToName = new HashMap<Integer, String>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<String>();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            lines.add(currentLine);
            currentLine = reader.readLine();
        }
        reader.close();

        // avoid clustering (data collection bias) by randomly parsing the
        // input lines of data
        Collections.shuffle(lines);

        String delimiters = "\\s+"; // one or more whitespace characters
        for (String line:lines) {
            String[] edge = line.split(delimiters);
            int fromIndex = getOrCreateIndex(edge[0], nameToIndex, indexToName);
            int toIndex = getOrCreateIndex(edge[1], nameToIndex, indexToName);

            // don't add self edges
            if (fromIndex != toIndex) {
                getAdjacencyList(fromIndex).add(toIndex);
                getAdjacencyList(toIndex).add(fromIndex);
            }
        }
    }

    // get index of a node given the node's name
    // create an entry if it does not exist
    private Integer getOrCreateIndex(String nodeName,
                                     Map<String, Integer> nameToIndex,
                                     Map<Integer, String> indexToName) {
        if (!nameToIndex.containsKey(nodeName)) {
            nameToIndex.put(nodeName, adjacencyLists.size());
            indexToName.put(adjacencyLists.size(), nodeName);
            adjacencyLists.add(new AdjacencyList());
        }
        return nameToIndex.get(nodeName);
    }
}