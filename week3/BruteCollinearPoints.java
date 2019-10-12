import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private static final Comparator<Point> natural = new Natural();

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points cannot be null");
        }

        int n = points.length;

        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
        }

        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = points[i];
        }
        Arrays.sort(pts);

        for (int i = 0; i < n - 1; i++) {
            Point a = pts[i];
            Point b = pts[i + 1];
            if (a.compareTo(b) == 0) {
                throw new IllegalArgumentException("Duplicate points are not allowed");
            }
        }

        if (n < 4) {
            return;
        }

        solve(pts);
    }

    private void solve(Point[] pts) {
        int n = pts.length;
        ArrayList<Point> sp = new ArrayList<Point>();
        for (int i = 0; i < n; i++) {
            Point a = pts[i];
            for (int j = i + 1; j < n; j++) {
                Point b = pts[j];
                for (int k = j + 1; k < n; k++) {
                    Point c = pts[k];
                    for (int h = k + 1; h < n; h++) {
                        Point d = pts[h];
                        double slope = a.slopeTo(b);
                        if (slope == a.slopeTo(c) && slope == a.slopeTo(d)) {
                            sp.clear();
                            sp.add(a);
                            sp.add(b);
                            sp.add(c);
                            sp.add(d);
                            sp.sort(natural);
                            Point first = sp.get(0);
                            Point last = sp.get(sp.size() - 1);
                            LineSegment segment = new LineSegment(first, last);
                            segments.add(segment);
                        }
                    }
                }
            }
        }
    }

    private static class Natural implements Comparator<Point> {
        public int compare(Point a, Point b) {
            return a.compareTo(b);
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] ls = new LineSegment[segments.size()];
        return segments.toArray(ls);
    }
}
