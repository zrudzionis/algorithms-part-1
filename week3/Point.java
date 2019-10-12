/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int dx = this.x - that.x;
        int dy = this.y - that.y;

        if (dy == 0 && dx == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (dy == 0) {
            return +0.0d;
        } else if (dx == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return 1.0d*dy / dx;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        int c = cmp(this.y, that.y);
        if (c != 0) {
            return c;
        } else {
            return cmp(this.x, that.x);
        }
    }

    private int cmp(int a, int b) {
        if (a < b) {
            return -1;
        } else if (b < a) {
            return 1;
        }
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }

    private class BySlope implements Comparator<Point> {
        public int compare(Point a, Point b) {
            double as = slopeTo(a);
            double bs = slopeTo(b);
            if (as < bs) {
                return -1;
            } else if (bs < as) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private static void test1() {
        StdOut.println("Test1...");
        Point a = new Point(1, 1);
        Point b = new Point(2, 1);
        Point c = new Point(3, 2);
        Point d = new Point(4, 3);
        Point[] ar = { d, c, b, a };
        Arrays.sort(ar, a.slopeOrder());
        assert ar[0].compareTo(new Point(1, 1)) == 0;
        assert ar[1].compareTo(new Point(2, 1)) == 0;
        assert ar[2].compareTo(new Point(3, 2)) == 0;
        assert ar[3].compareTo(new Point(4, 3)) == 0;
    }

    private static void test2() {
        StdOut.println("Test2...");
        Point a = new Point(1, 1);
        Point b = new Point(2, 1);
        assert a.compareTo(b) == -1;
        assert b.compareTo(a) == 1;
    }

    private static void test3() {
        StdOut.println("Test3...");
        Point a = new Point(0, 0);
        Point b = new Point(1, 1);
        assert a.slopeTo(b) == 1.0d;
    }

    private static void test4() {
        StdOut.println("Test4...");
        Point a = new Point(1, 1);
        Point b = new Point(1, 1);
        assert a.compareTo(b) == 0;
        assert b.compareTo(a) == 0;
    }

    private static void test5() {
        StdOut.println("Test5...");
        Point a = new Point(148, 178);
        Point b = new Point(1, 401);
        double slope = a.slopeTo(b);
        assert slope == -1.5170068027210883d : String.format("got %f", slope);
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        StdOut.println("Running tests:");
        test1();
        test2();
        test3();
        test4();
        test5();
    }
}
