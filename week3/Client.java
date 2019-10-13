import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Client {
    private static void drawPoints(Point[] points) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point p : points) {
            StdDraw.setPenRadius(0.01);
            int[] xy = toXY(p);
            StdDraw.text(xy[0], xy[1] + 1000, p.toString());
            StdDraw.setPenRadius(0.02);
            p.draw();
        }
        StdDraw.show();
    }

    private static int[] toXY(Point p) {
        String[] xystr = p.toString().split(",");
        String x = xystr[0].substring(1);
        String y = xystr[1].substring(1, xystr[1].length() - 1);
        int[] xy = {Integer.parseInt(x), Integer.parseInt(y)};
        return xy;
    }

    private static void drawSegments(LineSegment[] segments) {
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.RED);
        for (LineSegment segment : segments) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private static void setScale(int lx, int rx, int by, int ty) {
        StdDraw.setXscale(lx, rx);
        StdDraw.setYscale(by, ty);
    }

    private static Point[] readFromFile(String filename) {
        String filePath = "tests/" + filename;
        File file;
        Scanner sc = null;

        try {
            file = new File(filePath);
            sc = new Scanner(file);
        } catch (IOException e) {

        }
        assert sc != null;

        int n = sc.nextInt();
        Point[] pts = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Point p = new Point(x, y);
            pts[i] = p;
        }

        return pts;
    }

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

    private static void test2() {
        StdOut.println("Test2...");
        Point[] pts = {
                new Point(10000, 0),
                new Point(0, 10000),
                new Point(3000, 7000),
                new Point(7000, 3000),
                new Point(20000, 21000),
                new Point(3000, 4000),
                new Point(14000, 15000),
                new Point(6000, 7000),
        };

        BruteCollinearPoints slow = new BruteCollinearPoints(pts);
        FastCollinearPoints fast = new FastCollinearPoints(pts);

//        setScale(-1000, 22768, -1000, 22768);
//        drawPoints(pts);
//        drawSegments(slow.segments());
//        drawSegments(fast.segments());

        assert slow.numberOfSegments() == 2;
        assert fast.numberOfSegments() == 2;
    }

    private static void test3() {
        StdOut.println("Test3...");
        Point[] pts = readFromFile("horizontal5.txt");
        FastCollinearPoints fast = new FastCollinearPoints(pts);

//        setScale(-1000, 24000, 4000, 18000);
//        drawPoints(pts);
//        drawSegments(fast.segments());
        assert fast.numberOfSegments() == 5;
    }

    private static void test4() {
        StdOut.println("Test4...");
        Point[] pts = readFromFile("equidistant.txt");
        FastCollinearPoints fast = new FastCollinearPoints(pts);
        BruteCollinearPoints slow = new BruteCollinearPoints(pts);

//        setScale(-4000, 35000, -4000, 35000);
//        drawPoints(pts);
//        drawSegments(fast.segments());
//        drawSegments(slow.segments());
        assert fast.numberOfSegments() == 4;
    }

    private static void test5() {
        StdOut.println("Test5...");
        Point[] pts = readFromFile("oneline.txt");
        FastCollinearPoints fast = new FastCollinearPoints(pts);

//        setScale(-4000, 35000, -4000, 35000);
//        drawPoints(pts);
//        drawSegments(fast.segments());
//        drawSegments(slow.segments());
        assert fast.numberOfSegments() == 1;
    }

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
    }
}
