package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int N;
    private int T;
    private int numSites;
    private PercolationFactory pf;
    private double[] simulations;
    private static final double CI = 1.96;


    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        this.T = T;
        this.pf = pf;

        numSites = N * N;
        simulations = simMonteCarlo();
    }

    // Run one MoneteCarlo simulation
    private double oneMonteCarlo() {
        Percolation p = pf.make(N);
        while (!p.percolates()) {
            int row = StdRandom.uniform(0, N);
            int col = StdRandom.uniform(0, N);
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / numSites;
    }

    // Run T MoneCarlo simulations
    private double[] simMonteCarlo() {
        double[] sims = new double[T];
        for (int i = 0; i < T; i++) {
            sims[i] = oneMonteCarlo();
        }
        return sims;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(simulations);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(simulations);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - ((CI * stddev()) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + ((CI * stddev()) / Math.sqrt(T));
    }
}
