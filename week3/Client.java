import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Client {
    private static void test1() {
        StdOut.println("Test1...");
        Point[] pts = {
            new Point(1, 1),
            new Point(2, 1),
            new Point(3, 1),
            new Point(4, 1),
        };
        BruteCollinearPoints slow = new BruteCollinearPoints(pts);
        FastCollinearPoints fast = new FastCollinearPoints(pts);

        assert slow.numberOfSegments() == 1;
        assert fast.numberOfSegments() == 1;
    }

    public static void main(String[] args) {
        test1();
    }
}
