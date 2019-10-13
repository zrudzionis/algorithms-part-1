import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
        ArrayList<Pair> sp = new ArrayList<Pair>();
        ArrayList<ArrayList<Double>> slopes = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < n; i++) {
            pairs[i] = new Pair(pts[i], i);
            slopes.add(new ArrayList<Double>());
        }

        for (int i = 0; i < n - 3; i++) {
            Pair ap = pairs[i];
            Point a = ap.p;
            Arrays.sort(pairs, i + 1, n, new ByPointComparator(a.slopeOrder()));
            int j = i + 1;
            Double slope = null;
            sp.clear();
            sp.add(ap);

            while (j < n) {
                Pair bp = pairs[j];
                Point b = bp.p;
                int bi = bp.idx;
                double bs = a.slopeTo(b);

                if (Collections.binarySearch(slopes.get(bp.idx), bs) >= 0) {
                    j += 1;
                    continue;
                }

                if (slope == null || slope == bs) {
                    slope = bs;
                    sp.add(bp);
                } else {
                    if (sp.size() >= 4) {
                        addSegment(sp, slopes);
                        sp.clear();
                    }
                    sp.clear();
                    sp.add(ap);
                    sp.add(bp);
                    slope = bs;
                }
                j += 1;
            }
            if (sp.size() >= 4) {
                addSegment(sp, slopes);
                sp.clear();
            }
        }
    }

    private void addSegment(ArrayList<Pair> sp, ArrayList<ArrayList<Double>> slopes) {
        sp.sort(new ByPointComparator(natural));
        LineSegment ls = new LineSegment(sp.get(0).p, sp.get(sp.size() - 1).p);
        segments.add(ls);

        for (int i = 1; i < sp.size(); i++) {
            Pair ap = sp.get(i - 1);
            Point a = ap.p;
            Pair bp = sp.get(i);
            Point b = bp.p;
            double slope = a.slopeTo(b);
            slopes.get(bp.idx).add(slope);
            Collections.sort(slopes.get(bp.idx));
            if (i == 1) {
                slopes.get(ap.idx).add(slope);
                Collections.sort(slopes.get(ap.idx));
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
