// Driver.java

public class Driver {
    public static void main(String args[]) {
        // verify arguments
        if (args.length < 2) {
            System.out.println(
                "usage: Driver data_file motif_size [--show-results]");
            System.exit(-1);
        }

        // determine if results should be printed
        boolean showResults = false;
        if (args.length == 3 && args[2].equals("--show-results")) {
            showResults = true;
        }

        long start = System.currentTimeMillis();

        // run the program
        Main app = new Main(args[0], Integer.parseInt(args[1]), showResults);
        app.run();

        System.out.println(
            (System.currentTimeMillis() - start) + " milliseconds to " +
            "complete the program");
    }
}
