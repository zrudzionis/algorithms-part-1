
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;

    public KdTree() {

    }

    private class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private int size;

        Node(Point2D p) {
            point = p;
            size = 0;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        if (root == null) {
            return 0;
        }
        int s = 1;
        if (root.left != null) {
            s += root.left.size + 1;
        }
        if (root.right != null) {
            s += root.right.size + 1;
        }
        return s;
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Null not allowed");
        }
    }

    public void insert(Point2D p) {
        nullGuard(p);
        root = insert(root, p, true);
    }

    private Node insert(Node node, Point2D p, boolean useX) {
        if (node == null) {
            return new Node(p);
        }
        if (node.point.equals(p)) {
            return node;
        }
        double nv = node.point.x(), pv = p.x();
        if (!useX) {
            nv = node.point.y();
            pv = p.y();
        }
        if (pv <= nv) {
            node.left = insert(node.left, p, !useX);
        } else {
            node.right = insert(node.right, p, !useX);
        }
        int sz = 0;
        if (node.left != null) {
            sz += node.left.size + 1;
        }
        if (node.right != null) {
            sz += node.right.size + 1;
        }
        node.size = sz;
        return node;
    }

    public boolean contains(Point2D p) {
        nullGuard(p);
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean useX) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        }
        double nv = node.point.x(), pv = p.x();
        if (!useX) {
            nv = node.point.y();
            pv = p.y();
        }
        if (pv <= nv) {
            return contains(node.left, p, !useX);
        } else {
            return contains(node.right, p, !useX);
        }
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
//        for (Point2D p : ts) {
//            StdDraw.point(p.x(), p.y());
//        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        nullGuard(rect);
        Stack<Point2D> s = new Stack<Point2D>();
        if (root != null) {
            fillWithPoints(root, true, s, rect);
        }
        return s;
    }

    private void fillWithPoints(Node node, boolean useX, Stack<Point2D> s, RectHV rect) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            s.push(node.point);
            fillWithPoints(node.left, !useX, s, rect);
            fillWithPoints(node.right, !useX, s, rect);
        } else if (useX) {
            if (rect.xmin() <= node.point.x()) {
                fillWithPoints(node.left, !useX, s, rect);
            }
            if (rect.xmax() >= node.point.x()) {
                fillWithPoints(node.right, !useX, s, rect);
            }
        } else {
            if (rect.ymin() <= node.point.y()) {
                fillWithPoints(node.left, !useX, s, rect);
            }
            if (rect.ymax() >= node.point.y()) {
                fillWithPoints(node.right, !useX, s, rect);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        nullGuard(p);
        if (root == null) {
            return null;
        }
        RectHV r = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Point2D np = nearest(root, r, root.point, true, p);
        return np;
    }

    private Point2D nearest(Node node, RectHV r, Point2D np, boolean useX, Point2D p) {
        if (node == null) {
            return np;
        }
        if (r.distanceSquaredTo(p) >= np.distanceSquaredTo(p)) {
            return np;
        }
        Point2D cp = node.point;
        if (cp.distanceSquaredTo(p) < np.distanceSquaredTo(p)) {
            np = cp;
        }
        RectHV r1, r2;
        if (useX) {
            r1 = new RectHV(r.xmin(), r.ymin(), cp.x(), r.ymax());
            r2 = new RectHV(cp.x(), r.ymin(), r.xmax(), r.ymax());
        } else {
            r1 = new RectHV(r.xmin(), r.ymin(), r.xmax(), cp.y());
            r2 = new RectHV(r.xmin(), cp.y(), r.xmax(), r.ymax());
        }

        if (r1.distanceSquaredTo(p) < r2.distanceSquaredTo(p)) {
            np = nearest(node.left, r1, np, !useX, p);
            np = nearest(node.right, r2, np, !useX, p);
        } else {
            np = nearest(node.right, r2, np, !useX, p);
            np = nearest(node.left, r1, np, !useX, p);
        }
        return np;
    }

    private static void testSize1() {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.0, 0.0));
        tree.insert(new Point2D(0.0, 1.0));
        tree.insert(new Point2D(1.0, 0.0));
        tree.insert(new Point2D(1.0, 0.0));
        assert tree.size() == 3 : String.format("Expected size to be 3 got %d", tree.size());
    }

    public static void main(String[] args) {
        testSize1();
    }
}
