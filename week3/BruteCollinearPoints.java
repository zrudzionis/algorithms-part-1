import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        validateNotNull(points);
        Point[] pts = Arrays.copyOf(points, points.length);
        Arrays.sort(pts);
        validateNoDuplicates(pts);
        solve(pts);
    }

    private void validateNotNull(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points cannot be null");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
        }
    }

    private void validateNoDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            Point a = points[i];
            Point b = points[i + 1];
            if (a.compareTo(b) == 0) {
                throw new IllegalArgumentException("Duplicate points are not allowed");
            }
        }
    }

    private void solve(Point[] pts) {
        int n = pts.length;
        ArrayList<LineSegment> ls = new ArrayList<LineSegment>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int h = k + 1; h < n; h++) {
                        Point a = pts[i];
                        Point d = pts[h];
                        double slope = a.slopeTo(pts[j]);

                        if (slope == a.slopeTo(pts[k]) && slope == a.slopeTo(d)) {
                            LineSegment segment = new LineSegment(a, d);
                            ls.add(segment);
                        }
                    }
                }
            }
        }

        segments = ls.toArray(new LineSegment[ls.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
}
