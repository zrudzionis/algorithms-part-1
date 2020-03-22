
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {
    private final TreeSet<Point2D> ts;

    public PointSET() {
        ts = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return ts.isEmpty();
    }

    public int size() {
        return ts.size();
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Null not allowed");
        }
    }

    public void insert(Point2D p) {
        nullGuard(p);
        ts.add(p);
    }

    public boolean contains(Point2D p) {
        nullGuard(p);
        return ts.contains(p);
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : ts) {
            StdDraw.point(p.x(), p.y());
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        nullGuard(rect);
        SET<Point2D> s = new SET<Point2D>();
        for (Point2D p : ts) {
            if (rect.contains(p)) {
                s.add(p);
            }
        }
        return s;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        nullGuard(p);
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D cp : ts) {
            double d = p.distanceSquaredTo(cp);
            if (d < minDistance) {
                minDistance = d;
                minPoint = cp;
            }
        }
        return minPoint;

    }

    public static void main(String[] args) {
        // TODO unit test
    }
}
