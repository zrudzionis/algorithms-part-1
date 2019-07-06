import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double stdDev = 0;
    private double mean = 0;
    private int trials;
    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("Invalid n or trials");
        }

        this.trials = trials;
        this.n = n;
        runTrials();
    }

    private void runTrials() {
        int[] openOrder = new int[n * n];
        double[] percolationVals = new double[trials];

        for (int i = 0; i < n * n; i++) {
            openOrder[i] = i;
        }

        for (int i = 0; i < trials; i += 1) {
            Percolation p = new Percolation(n);
            StdRandom.shuffle(openOrder);

            for (int j = 0; j < n * n; j += 1) {
                int row = openOrder[j] / n + 1;
                int col = openOrder[j] % n + 1;

                p.open(row, col);
                if (p.percolates()) {
                    break;
                }
            }

            double pVal = 1.0d * p.numberOfOpenSites() / (n * n);
            percolationVals[i] = pVal;
        }
        mean = StdStats.mean(percolationVals);
        stdDev = StdStats.stddev(percolationVals);
    }


    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - 1.96d * stdDev / Math.sqrt(trials);

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + 1.96d * stdDev / Math.sqrt(trials);
    }

    private static boolean equals(double a, double b, double delta) {
        return Math.abs(a - b) < delta;
    }

    private static void testPercolationStats() {
        PercolationStats stats = new PercolationStats(2, 10000);
        assert equals(stats.mean(), 0.66d, 0.01) : stats.mean();
    }

    // test client (see below)
    public static void main(String[] args) {
        testPercolationStats();
    }
}
