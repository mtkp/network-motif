// AdjacencyMatrix.java

// Undirected graph representation.

import java.util.BitSet;

public class AdjacencyMatrix {

    private int order;
    private BitSet matrix;

    public AdjacencyMatrix(int order) {
        this.order = order;
        this.matrix = new BitSet((order * (order - 1)) / 2);
    }

    private AdjacencyMatrix(AdjacencyMatrix source) {
        this.order = source.order;
        this.matrix = (BitSet)source.matrix.clone();
    }

    public void addEdge(int x, int y) {
        if (x == y) {
            return;
        }
        matrix.set(indexFor(x, y));
    }

    public boolean hasEdge(int x, int y) {
        if (x == y) {
            return true;
        }
        return matrix.get(indexFor(x, y));
    }

    public AdjacencyMatrix copy() {
        return new AdjacencyMatrix(this);
    }

    private int indexFor(int x, int y) {
        int n = Math.max(x, y);
        return ((n * (n - 1)) / 2) + Math.min(x, y);
    }

    public byte[] toBytes() {
        // Byte representation is per the graph6 format specfication
        // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

        // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)
        byte[] orderBytes = convertOrderToBytes(order);

        int bitVectorLength = (order * (order - 1)) / 2;
        int outputLength = orderBytes.length + (bitVectorLength / 6)
                           + ((bitVectorLength % 6) > 0 ? 1 : 0);

        byte[] output = new byte[outputLength];
        System.arraycopy(orderBytes, 0, output, 0, orderBytes.length);

        int currentBit = 0;
        int currentIndex = orderBytes.length;
        byte currentByte = 0;
        for (int col = 1; col < order; col++) {
            for (int row = 0; row < col; row++) {

                // switch to simple loop

                if (hasEdge(row, col)) {
                    currentByte = (byte)(currentByte | (1 << (5 - currentBit)));
                }

                // increment the bit
                currentBit = (currentBit + 1) % 6;

                if (currentBit == 0) {
                    // add byte to output (increment by 63
                    // according to the graph6 algorithm)
                    output[currentIndex] = (byte)(currentByte + 63);
                    currentIndex++;
                    currentByte = 0;
                }
            }
        }

        // complete last byte
        if (currentIndex < outputLength) {
            output[currentIndex] = (byte) (currentByte + 63);
        }

        return output;
    }

    private static byte[] convertOrderToBytes(int order) {
        // Per the graph6 format specfication
        // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

        // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)

        byte[] bytes;

        if (order <= 62) {
            bytes = new byte[1];
            bytes[0] = (byte) (order + 63);
        } else if (order <= 258047) {
            bytes = new byte[4];
            bytes[0] = 126;
            bytes[1] = (byte) ((order >>> 12) & 63);
            bytes[2] = (byte) ((order >>> 6) & 63);
            bytes[3] = (byte) (order & 63);
        } else {
            bytes = new byte[8];
            bytes[0] = 126;
            bytes[1] = 126;
            bytes[2] = (byte) ((order >>> 30) & 63);
            bytes[3] = (byte) ((order >>> 24) & 63);
            bytes[4] = (byte) ((order >>> 18) & 63);
            bytes[5] = (byte) ((order >>> 12) & 63);
            bytes[6] = (byte) ((order >>> 6) & 63);
            bytes[7] = (byte) (order & 63);
        }

        return bytes;
    }
}