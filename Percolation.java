package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private boolean[][] sites;
    private int openSites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF backwash;
    private int top;
    private int bottom;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        sites = new boolean[N][N];
        openSites = 0;
        uf = new WeightedQuickUnionUF(N * N + 2);
        backwash = new WeightedQuickUnionUF(N * N + 1);
        top = 0;
        bottom = N * N + 1;
    }

    private void validate(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
    }

    // return index in WeightedQuickUnionUF object in specific row and column
    private int ufIndex(int row, int col) {
        return N * row + col + 1;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) {
            return;
        }
        sites[row][col] = true;
        openSites++;

        int current = ufIndex(row, col);
        // connect to top
        if (row == 0) {
            uf.union(current, top);
            backwash.union(current, top);
        }
        // connect to bottom
        if (row == N - 1) {
            uf.union(current, bottom);
        }

        // connect to neighbors
        if (col < N - 1 && isOpen(row, col + 1)) {
            int rightNeighbor = ufIndex(row, col + 1);
            uf.union(current, rightNeighbor);
            backwash.union(current, rightNeighbor);
        }
        if (row < N - 1  && isOpen(row + 1, col)) {
            int bottomNeighbor = ufIndex(row + 1, col);
            uf.union(current, bottomNeighbor);
            backwash.union(current, bottomNeighbor);
        }
        if (col > 0 && isOpen(row, col - 1)) {
            int leftNeighbor = ufIndex(row, col - 1);
            uf.union(current, leftNeighbor);
            backwash.union(current, leftNeighbor);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            int topNeighbor = ufIndex(row - 1, col);
            uf.union(current, topNeighbor);
            backwash.union(current, topNeighbor);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int current = ufIndex(row, col);
        return uf.connected(current, top) && backwash.connected(current, top);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // use for unit testing (not required, but keep this here for the autograder)
    public static void main(String[] args) {
    }
}
