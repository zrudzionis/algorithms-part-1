import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
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
        ArrayList<LineSegment> ls = new ArrayList<LineSegment>();
        int n = pts.length;
        for (int i = 0; i < n - 3; i++) {
            Arrays.sort(pts);
            Arrays.sort(pts, pts[i].slopeOrder());
            Point origin = pts[0];

            for (int first = 1, last = 2; last < n; last++) {
                Point fp = pts[first];

                while (last < n && origin.slopeTo(fp) == origin.slopeTo(pts[last])) {
                    last += 1;
                }

                if (last - first > 2 && origin.compareTo(fp) < 0) {
                    ls.add(new LineSegment(origin, pts[last - 1]));
                }

                first = last;
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
