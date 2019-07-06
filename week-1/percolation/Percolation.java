/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int top = 0;
    private final int bottom = 0;

    private boolean[][] gridOpen;
    private int openSites = 0;
    private WeightedQuickUnionUF topWqu;
    private WeightedQuickUnionUF bottomWqu;
    private int n;
    private boolean doPercolate = false;

    // creates n-by-n gridOpen, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Invalid n");
        }

        this.n = n;
        gridOpen = new boolean[n + 1][n + 1];
        topWqu = new WeightedQuickUnionUF(n * n + 1);
        bottomWqu = new WeightedQuickUnionUF(n * n + 1);
    }

    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1) + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid col or row");
        }
        if (isOpen(row, col)) {
            return;
        }

        int index = getIndex(row, col);
        this.gridOpen[row][col] = true;
        this.openSites += 1;
        
        if (row == 1) {
            topWqu.union(top, index);
        }
        if (row == n) {
            bottomWqu.union(bottom, index);
        }

        for (int i = -1; i < 2; i += 2) {
            int nextRow = row + i;
            int nextCol = col + i;
            if (nextRow > 0 && nextRow <= n && this.isOpen(nextRow, col)) {
                connect(row, col, nextRow, col);
            }
            if (nextCol > 0 && nextCol <= n && this.isOpen(row, nextCol)) {
                connect(row, col, row, nextCol);
            }
        }

        if (!doPercolate && topWqu.connected(top, index) && bottomWqu.connected(bottom, index)) {
            doPercolate = true;
        }
    }

    private void connect(int row1, int col1, int row2, int col2) {
        int ind1 = getIndex(row1, col1);
        int ind2 = getIndex(row2, col2);
        topWqu.union(ind1, ind2);
        bottomWqu.union(ind1, ind2);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid col or row");
        }
        return gridOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Invalid col or row");
        }
        int index = this.getIndex(row, col);
        return topWqu.connected(top, index);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return doPercolate;
    }

    ///////////////////////// Tests /////////////////////////////

    private static void testIsFull() {
        Percolation p = new Percolation(6);
        p.open(1, 6);
        assert p.numberOfOpenSites() == 1;
        assert p.isFull(1, 6);
    }

    private static void testPercolatesEdgeCase() {
        Percolation p = new Percolation(1);
        assert !p.percolates();
    }

    private static void testPercolatesEdgeCaseWithOpenSite() {
        Percolation p = new Percolation(1);
        p.open(1, 1);
        assert p.numberOfOpenSites() == 1;
        assert p.percolates();
    }

    private static void testOpeningNewSiteConnects() {
        Percolation p = new Percolation(3);
        p.open(1, 1);
        assert p.numberOfOpenSites() == 1;
        assert p.isFull(1, 1);
        p.open(1, 3);
        assert p.numberOfOpenSites() == 2;
        assert p.isFull(1, 3);
        p.open(1, 2);
        assert p.numberOfOpenSites() == 3;
        assert p.isFull(1, 2);
        p.open(1, 2);
        assert p.numberOfOpenSites() == 3;
        assert p.isFull(1, 2);
    }

    private static void testFullAndOpenConsistent() {
        Percolation p = new Percolation(3);
        p.open(2, 3);
        assert p.numberOfOpenSites() == 1;
        assert !p.isFull(2, 3);
        assert p.isOpen(2, 3);
    }

    private static void testFullAndOpenConsistentForClosedSite() {
        Percolation p = new Percolation(3);
        assert p.numberOfOpenSites() == 0;
        assert !p.isFull(3, 1);
        assert !p.isOpen(3, 1);
    }


    private static void openAndAssert(int row, int col, boolean isFull, Percolation p) {
        p.open(row, col);
        assert isFull == p.isFull(row, col);
        assert p.isOpen(row, col);
    }

    private static void testFullAndOpenConsistentForClosedSiteAndLargerSquare() {
        Percolation p = new Percolation(6);
        assert p.numberOfOpenSites() == 0;
        assert !p.isFull(2, 3);
        assert !p.isOpen(2, 3);
    }

    private static void testLargerSet() {
        Percolation p = new Percolation(6);

        openAndAssert(1, 6, true, p);
        openAndAssert(2, 6, true, p);
        openAndAssert(3, 6, true, p);
        openAndAssert(4, 6, true, p);
        openAndAssert(5, 6, true, p);
        openAndAssert(5, 5, true, p);
        openAndAssert(4, 4, false, p);
        openAndAssert(3, 4, false, p);
        openAndAssert(2, 4, false, p);
        openAndAssert(2, 3, false, p);
        openAndAssert(2, 2, false, p);
        openAndAssert(2, 1, false, p);
        openAndAssert(3, 1, false, p);
        openAndAssert(4, 1, false, p);
        openAndAssert(5, 1, false, p);
        openAndAssert(5, 2, false, p);
        openAndAssert(6, 2, false, p);
        openAndAssert(5, 4, true, p);
        assert p.percolates();
    }

    private static void testGetIndexProducesUniqueValuesForSmallInput() {
        Percolation p = new Percolation(2);
        assert p.getIndex(1, 2) != p.getIndex(2, 1);
    }

    private static void testGetIndexProducesUniqueValues() {
        int n = 6;
        Percolation p = new Percolation(n);
        boolean[] marked = new boolean[n * n + 1];
        for (int i = 1; i <= n; i += 1) {
            for (int j = 1; j <= n; j += 1) {
                int index = p.getIndex(i, j);
                assert !marked[index] :
                        String.format("Produced duplicate at row: %d col: %d with index: %d", i,
                                      j,
                                      index);
                marked[index] = true;
            }
        }
    }

    private static void testThrowsExceptionOnInvalidInput() {
        try {
            new Percolation(0);
            assert false : "Should raise IllegalArgumentException";
        }
        catch (IllegalArgumentException e) {

        }
    }

    private static void testBackWash() {
        Percolation p = new Percolation(3);

        openAndAssert(1, 3, true, p);
        openAndAssert(2, 3, true, p);
        assert !p.percolates();
        openAndAssert(3, 3, true, p);
        assert p.percolates();
        openAndAssert(3, 1, false, p);
        openAndAssert(2, 1, false, p);
        openAndAssert(1, 1, true, p);
        assert p.percolates();
    }

    private static void testBackWash2() {
        Percolation p = new Percolation(7);

        openAndAssert(6, 1, false, p);
        openAndAssert(7, 1, false, p);
        openAndAssert(7, 2, false, p);
        openAndAssert(7, 4, false, p);
        openAndAssert(1, 1, true, p);
        openAndAssert(1, 5, true, p);
        openAndAssert(2, 5, true, p);
        openAndAssert(3, 5, true, p);
        openAndAssert(4, 5, true, p);
        openAndAssert(5, 5, true, p);
        openAndAssert(6, 5, true, p);
        assert !p.percolates();
        openAndAssert(7, 5, true, p);
        assert p.percolates();
        openAndAssert(2, 1, true, p);
        openAndAssert(4, 1, false, p);
        openAndAssert(5, 1, false, p);
        openAndAssert(3, 1, true, p);
        assert p.percolates();
    }

    // test client (optional)
    public static void main(String[] args) {
        testIsFull();
        testPercolatesEdgeCase();
        testPercolatesEdgeCaseWithOpenSite();
        testOpeningNewSiteConnects();
        testFullAndOpenConsistent();
        testFullAndOpenConsistentForClosedSite();
        testFullAndOpenConsistentForClosedSiteAndLargerSquare();
        testGetIndexProducesUniqueValuesForSmallInput();
        testGetIndexProducesUniqueValues();
        testLargerSet();
        testThrowsExceptionOnInvalidInput();
        testBackWash();
        testBackWash2();
    }
}
