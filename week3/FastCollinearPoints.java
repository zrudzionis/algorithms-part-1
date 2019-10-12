import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private static final Comparator<Point> natural = new Natural();

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
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
        Pair[] pairs = new Pair[n];
        for (int i = 0; i < n; i++) {
            pairs[i] = new Pair(pts[i], i);
        }
        boolean vi[] = new boolean[n];
        ArrayList<Pair> sp = new ArrayList<Pair>();

        for (int i = 0; i < n - 3; i++) {
            Pair ap = pairs[i];
            Point a = ap.p;
            Arrays.sort(pairs, i + 1, n - 1, new ByPointComparator(a.slopeOrder()));

            int j = i + 1;
            Double slope = null;
            sp.clear();
            sp.add(ap);

            while (j < n) {
                Pair bp = pairs[j];
                Point b = bp.p;
                int bi = bp.idx;
                double bs = a.slopeTo(b);

                if (vi[bi]) {
                    j += 1;
                    continue;
                }

                if (slope == null || slope == bs) {
                    slope = bs;
                    sp.add(bp);
                } else {
                    if (sp.size() >= 4) {
                        addSegment(sp, vi);
                    }
                    sp.add(ap);
                    sp.add(bp);
                    slope = bs;
                }
                j += 1;
            }
            if (sp.size() >= 4) {
                addSegment(sp, vi);
            }
        }
    }

    private void addSegment(ArrayList<Pair> sp, boolean vi[]) {
        sp.sort(new ByPointComparator(natural));
        LineSegment ls = new LineSegment(sp.get(0).p, sp.get(sp.size() - 1).p);
        segments.add(ls);
        for(int i = 0; i < sp.size(); i++) {
            vi[sp.get(i).idx] = true;
        }
        sp.clear();
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


    private class Pair {
        public Point p;
        public int idx;

        public Pair(Point p, int idx) {
            this.p = p;
            this.idx = idx;
        }
    }

    private class ByPointComparator implements Comparator<Pair> {
        private Comparator<Point> cmp;

        public ByPointComparator(Comparator<Point> cmp) {
            this.cmp = cmp;
        }

        public int compare(Pair a, Pair b) {
            return cmp.compare(a.p, b.p);
        }
    }
}
