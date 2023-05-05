package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    // A helper function to call addLast N times
    private static void addNLast(AList<Integer> targetAList, int desiredN, AList<Double> timeReadsStorage) {
        // start the stopwatch
        Stopwatch sw = new Stopwatch();
        // A loop from 0~N
        for (int i = 0; i < desiredN; i += 1) {
            // call addLast
            targetAList.addLast(1);
        }
        // read the stopwatch
        double timeInSeconds = sw.elapsedTime();
        // store the read from stopwatch
        timeReadsStorage.addLast(timeInSeconds);
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE

        // the Ns AList
        AList<Integer> Ns = new AList();
        // the times AList
        AList<Double> times = new AList();
        // the generic Ns List
        int[] oneKto128K = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        // iterate through the generic Ns List
        for (int j : oneKto128K) {
            // add the items to AList
            Ns.addLast(j);
            // init a temp AList
            AList<Integer> temp = new AList();
            // add Last for each item in oneKto128K
            addNLast(temp, j, times);
        }
        printTimingTable(Ns, times, Ns);
    }
}
