// Labeler.java

import java.io.*;
import java.util.*;

public class Labeler {

    // labelg program options
    private static final String programName = "./labelg";
    private static final int invariant = 3;
    private static final int mininvarlevel = 1;
    private static final int maxinvarlevel = 100;

    // file options
    private static final String filePrefix  = ".";
    private static final String filePostfix = ".g6";

    // data members
    private String inputFilename;
    private String outputFilename;
    private String[] args;

    public Labeler() {
        long currentTime = System.currentTimeMillis();
        this.inputFilename = filePrefix + "rawgraph6_" +
                             currentTime + filePostfix;
        this.outputFilename = filePrefix + "canonical_" +
                              currentTime + filePostfix;
        this.args = getArgs();
    }

    private String[] getArgs() {
        String[] args = {
            programName,
            "-i" + invariant,
            "-I" + mininvarlevel + ":" + maxinvarlevel,
            inputFilename,
            outputFilename};
        return args;
    }

    public Map<String, Integer> getCanonicalLabels(
        Map<String, Integer> subgraphs) {
        // Get canonical labels using the labelg program.
        // This function communicates with labelg using input and output files.
        //
        // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)

        BufferedWriter writer = null;
        BufferedReader inputReader = null;
        BufferedReader outputReader = null;
        Map<String, Integer> labels = new HashMap<String, Integer>();
        int returnCode = 0;
        try {
            writer = new BufferedWriter(new FileWriter(inputFilename));
            for (String graph:subgraphs.keySet()) {
                writer.write(graph);
                writer.write('\n');
            }
            writer.close();

            Process labelg = Runtime.getRuntime().exec(args);

            // close output stream, input stream & error stream
            labelg.getOutputStream().close();
            closeInputStream(labelg.getInputStream());
            closeInputStream(labelg.getErrorStream());

            // wait for labelg to complete execution
            returnCode = labelg.waitFor();

            // read back in the input and output file
            inputReader = new BufferedReader(new FileReader(inputFilename));
            outputReader = new BufferedReader(new FileReader(outputFilename));
            String inputLine = inputReader.readLine();
            String outputLine = outputReader.readLine();

            // combine the input and output, assuming labelg writes output
            // in the same order as the input is provided
            while (outputLine != null) {
                int count = subgraphs.get(inputLine);
                if (labels.containsKey(outputLine)) {
                    count += labels.get(outputLine);
                }
                labels.put(outputLine, count);
                inputLine = inputReader.readLine();
                outputLine = outputReader.readLine();
            }
            inputReader.close();
            outputReader.close();
        } catch (Exception e) {
            System.err.println("Exception " + e + " raised");
            e.printStackTrace();
            labels = null;
        } finally {
            try { writer.close(); } catch (Exception e) {}
            try { inputReader.close(); } catch (Exception e) {}
            try { outputReader.close(); } catch (Exception e) {}
            deleteFile(inputFilename);
            deleteFile(outputFilename);
        }

        if (labels == null) {
            System.exit(-1);
        }

        if (returnCode != 0) {
            System.err.println("`labelg` exited with a return code of: " +
                returnCode);
            System.err.print("`labelg` executed with: ");
            for (String line:args) {
                System.err.print(line + " ");
            }
            System.err.println();
            System.exit(-1);
        }

        return labels;
    }

    private void closeInputStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String l = r.readLine();
        while (l != null) {
            l = r.readLine();
        }
        r.close();
    }

    private void deleteFile(String filename) {
        try {
            File f = new File(filename);
            f.delete();
        } catch (Exception e) {
            System.err.println("Exception " + e +
                " raised when attempting to delete " + filename);
        }
    }

}