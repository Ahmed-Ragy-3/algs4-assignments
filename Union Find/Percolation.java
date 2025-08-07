import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// goal is to estimate p* by Monte Carlo simulation
/* 
for n = 5

         *

   0  1  2  3  4
   5  6  7  8  9
   10 11 12 13 14 
   15 16 17 18 19
   20 21 22 23 24

         *
   model[r][c] = map(r,c)
*/
public class Percolation {
   private boolean grid[][];
   private WeightedQuickUnionUF uf;
   private int n, opened;

   private int top;
   private int bottom;

   // creates n-by-n grid, with all sites initially blocked
   public Percolation(int n) {
      if (n <= 0)
         throw new IllegalArgumentException("size of grid must be greater than 0");
      
      this.n = n;
      this.opened = 0;
      this.grid = new boolean[n][n];
      this.uf = new WeightedQuickUnionUF(n * n + 2);
      this.top = n * n;
      this.bottom = n * n + 1;

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            this.grid[i][j] = false;
         }
         uf.union(top, map(0, i)); // connect top with the upper row
         uf.union(bottom, map(n - 1, i)); // connect bottom with the lower row
      }
   }

   private int map(int row, int col) {
      return row * n + col;
   }

   private boolean isConnected(int p, int q) { // take indices
      return uf.find(p) == uf.find(q);
   }

   private void check(int r, int c) {
      if (!(1 <= r && r <= n && 1 <= c && c <= n))
         throw new IllegalArgumentException("indices are out of range");
   }

   private boolean valid(int r, int c) {
      if (r < 0 || r >= n || c < 0 || c >= n)
         return false;
      return grid[r][c];
   }

   // opens the site (row, col) if it is not open already
   public void open(int row, int col) {
      check(row, col);
      row--;
      col--;

      if (grid[row][col] == false) {
         opened++;
         grid[row][col] = true;

         if (valid(row + 1, col))
            uf.union(map(row, col), map(row + 1, col)); // down

         if (valid(row - 1, col))
            uf.union(map(row, col), map(row - 1, col)); // up

         if (valid(row, col - 1))
            uf.union(map(row, col), map(row, col - 1)); // left

         if (valid(row, col + 1))
            uf.union(map(row, col), map(row, col + 1)); // right

      }
   }

   // is the site (row, col) open?
   public boolean isOpen(int row, int col) {
      check(row, col);
      return grid[row - 1][col - 1];

   }

   // is the site (row, col) full?
   public boolean isFull(int row, int col) {
      //check(row, col);
      if(isOpen(row, col)) {
         return isConnected(top, map(row-1, col-1));
      }

      return false;
   }

   // returns the number of open sites
   public int numberOfOpenSites() {
      return opened;
   }

   // does the system percolate?
   public boolean percolates() {
      return isConnected(top, bottom);
   }

   // test client (optional)
   // public static void main(String[] args) {
   // Percolation system = new Percolation(StdIn.readInt()) ;
   // List<int[]> blockedSites = new ArrayList<>();

   // for (int r=1 ; r <= system.n ; r++) {
   // for (int c=1 ; c <= system.n ; c++) {
   // blockedSites.add(new int[]{r, c}) ;
   // }
   // }

   // int[] site ;
   // while (!system.percolates()) {
   // // Choose a site uniformly at random among all blocked sites
   // site = blockedSites.remove(StdRandom.uniformInt(blockedSites.size())) ;
   // // open the site
   // system.open(site[0] , site[1]);
   // }

   // double p_th = system.numberOfOpenSites() / (double)(system.n * system.n) ;
   // System.out.println("Estimate of the percolation threshold: " + p_th);
   
   // Percolation sys = new Percolation(2) ;
   // // sys.open(2, 2);
   // // sys.open(1, 2);
   // System.out.println(sys.isFull(1,1));
   // }


}