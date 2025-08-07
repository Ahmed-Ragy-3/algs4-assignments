//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private int trials;
  private double p_th[];

  private final double CONFIDENCE_95 = 1.96;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0)
      throw new IllegalArgumentException("out of range");

    this.trials = trials;
    p_th = new double[trials];


    //Percolation system = new Percolation(n);

    int[][] blockedSites = new int[n * n][2];
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        blockedSites[r * n + c][0] = r + 1;
        blockedSites[r * n + c][1] = c + 1;
      }
    }

    int[] site = new int[2];
    for (int i = 0; i < trials; i++) {
      Percolation system = new Percolation(n);
      int range = n * n - 1;
      while (!system.percolates()) {
        site = getSite(StdRandom.uniformInt(0, range), blockedSites, range);
        range--;
        system.open(site[0], site[1]);
      }
      this.p_th[i] = (double) system.numberOfOpenSites() / (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(p_th);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(p_th);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    double x_bar = mean();
    double s = stddev();
    return x_bar - ((CONFIDENCE_95 * s) / Math.sqrt(trials));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    double x_bar = mean();
    double s = stddev();
    return x_bar + ((CONFIDENCE_95 * s) / Math.sqrt(trials));
  }

  private static int[] getSite(int rand, int[][] blockedSites, int end) {
    int[] temp_arr = blockedSites[rand];
    blockedSites[rand] = blockedSites[end];
    blockedSites[end] = temp_arr;

    return blockedSites[end];
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    // int n = StdIn.readInt();
    // int trials = StdIn.readInt();

    PercolationStats stat = new PercolationStats(n, trials);

    System.out.println("mean                    = " + stat.mean());
    System.out.println("stddev                  = " + stat.stddev());
    System.out.println("95% confidence interval = [" + stat.confidenceLo() + ", " + stat.confidenceHi() + "]");

  }
}