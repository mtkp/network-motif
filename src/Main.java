// Main.java

import java.io.IOException;
import java.util.*;

public class Main {

    // app execution code goes here
    public void run() {
        long start = System.currentTimeMillis();

        Graph graph = null;

        try {
            graph = new Graph(filename);
        } catch (IOException e) {
            System.out.println("Unable to parse data file");
            return;
        }
        System.out.println(
            (System.currentTimeMillis() - start) + " milliseconds to " +
            "generate a network of size " + graph.size());

        System.out.println("Executing ESU for motif size " + motifSize + "...");
        start = System.currentTimeMillis();

        // execute ESU
        Map<String, Integer> subgraphs = new HashMap<String, Integer>();
        for (int i = 0; i < graph.size(); i++) {
            graph.enumerate(i, motifSize, subgraphs);
        }

        System.out.println(
            (System.currentTimeMillis() - start) + " milliseconds to " +
            "execute ESU.");

        System.out.println("Determining subgraph (label) frequency...");
        start = System.currentTimeMillis();

        // get canonical labels through `labelg`
        Labeler labeler = new Labeler();
        Map<String, Integer> labels = labeler.getCanonicalLabels(subgraphs);

        System.out.println(
            (System.currentTimeMillis() - start) + " milliseconds to " +
            "determine subgraph (label) frequency.");

        if (showResults) {
            System.out.println("Label\tFrequency");
            for (Map.Entry<String, Integer> entry:labels.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue());
            }
        }
    }

    private String filename;
    private int motifSize;
    private boolean showResults;
    public Main(String filename, int motifSize, boolean showResults) {
        this.filename = filename;
        this.motifSize = motifSize;
        this.showResults = showResults;
    }
}
