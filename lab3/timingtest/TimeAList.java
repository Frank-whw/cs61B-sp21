package timingtest;
import afu.org.checkerframework.checker.units.qual.A;
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

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>(); //use to document
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        //int[] sizeOfDate = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        int[] sizeOfDate = {100000, 200000, 400000, 800000, 1600000, 3200000, 6400000, 12800000};
        for(int N : sizeOfDate){
            AList<Integer> temperList = new AList<>();
            Stopwatch sw = new Stopwatch();//how should i use it? ans: As a starting signal
            //and then i need to test the speed of addLast
            for(int i = 0; i < N; i += 1){
                temperList.addLast(i);//whatever the num it is
            }
            double timeInSeconds = sw.elapsedTime();//As a final signal

            Ns.addLast(N);
            times.addLast(timeInSeconds);
            opCounts.addLast(N);
        }

        printTimingTable(Ns, times, opCounts);//so I need to creat these parameters by myself. wtf??!
    }

}
