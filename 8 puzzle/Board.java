import java.util.Iterator;

import edu.princeton.cs.algs4.In;

public class Board {

   private int n;
   private int[][] board;
   private final int BLANK = 0;
   // create a board from an n-by-n array of tiles,
   // where tiles[row][col] = tile at (row, col)
   public Board(int[][] tiles) {
      this.n = tiles.length;
      board = new int[n][n];
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            board[i][j] = tiles[i][j];
         }
      }
   }
                                          
   // string representation of this board
   public String toString() {
      StringBuilder strb = new StringBuilder();
      strb.append(n);
      strb.append("\n");
      for(int[] row : board) {
         for(int i : row) {
            strb.append(" " + i);
         }
         strb.append("\n");
      }
      return strb.toString();
   }

   // board dimension n
   public int dimension() {
      return n;
   }

   // number of tiles out of place
   public int hamming() {
      int ham = 0;
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            if(board[i][j] != (n * i) + (j + 1)) {
               if(board[i][j] == BLANK) continue;
               // System.out.println("wrong position for" + board[i][j]);
               ham++;
            }
         }
      }
      return ham;
   }

   private int manhattan_dis(int i, int j) {
      // i & j are indexes
      if(board[i][j] == BLANK) {
         return 0;
         // return Math.abs(i - (n - 1)) + Math.abs(j - (n - 1));
      }
      int valid_row = (board[i][j] - 1) / n;
      int valid_col = (board[i][j] - 1) % n;

      return Math.abs(i - valid_row) + Math.abs(j - valid_col);
   }

   // sum of Manhattan distances between tiles and goal
   public int manhattan() {
      int man = 0;
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            man += manhattan_dis(i, j);
         }
      }
      return man;
   }

   // is this board the goal board?
   public boolean isGoal() {
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            if(board[i][j] != (n * i) + (j + 1)) {
               if(i == n - 1 && j == n - 1 && board[i][j] == BLANK) return true;
               return false;
            }
         }
      }
      return true;
   }

   // does this board equal y?
   public boolean equals(Object y) {
      if(this == y) return true;
      if(y == null || getClass() != y.getClass()) return false;
      Board that = (Board) y;

      if(this.n != that.n) return false;

      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            if(this.board[i][j] != that.board[i][j]) return false;
         }
      }
      return true;
   }

   private Board get_neighbor(int i1, int j1, int i2, int j2) {

      Board neighbor = new Board(board);
      neighbor.board[i1][j1] = board[i2][j2];
      neighbor.board[i2][j2] = board[i1][j1];

      return neighbor;
   }
   // all neighboring boards
   public Iterable<Board> neighbors() {
      int i1 = 0;
      int j1 = 0;
      for(int i = 0; i < n; i++) {
         boolean end = false;
         for(int j = 0; j < n; j++) {
            if(board[i][j] == BLANK) {
               i1 = i;
               j1 = j;
               end = true;
               break;
            }
         }
         if(end) break;
      }

      Board[] arr = new Board[4];
      int ind = 0;
      if(i1 - 1 >= 0) {
         arr[ind++] = get_neighbor(i1, j1, i1 - 1, j1);
      }
      if(i1 + 1 < n) {
         arr[ind++] = get_neighbor(i1, j1, i1 + 1, j1);
      }
      if(j1 - 1 >= 0) {
         arr[ind++] = get_neighbor(i1, j1, i1, j1 - 1);
      }
      if(j1 + 1 < n) {
         arr[ind++] = get_neighbor(i1, j1, i1, j1 + 1);
      }

      return new Iterable<Board>() {
         @Override
         public Iterator<Board> iterator() {
            return new Iterator<Board>() {
               private int index = 0;
               @Override
               public boolean hasNext() {
                  return index < 4 && arr[index] != null;
               }

               @Override
               public Board next() {
                  return arr[index++];
               }
            };
         }
      };
   }

   // a board that is obtained by exchanging any pair of tiles
   public Board twin() {
      int[][] twinTiles = new int[n][n];
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            twinTiles[i][j] = board[i][j];
         }
      }
  
      // Swap any two non-blank tiles
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n - 1; j++) {
            if(twinTiles[i][j] != BLANK && twinTiles[i][j + 1] != BLANK) {
               int temp = twinTiles[i][j];
               twinTiles[i][j] = twinTiles[i][j + 1];
               twinTiles[i][j + 1] = temp;
               return new Board(twinTiles);
            }
         }
      }
      return new Board(twinTiles);
   }

   // unit testing (not graded)
   public static void main(String[] args) {
      // create initial board from file
      In in = new In(args[0]);
      
      int n = in.readInt();
      int[][] tiles = new int[n][n];

      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++) {
            tiles[i][j] = in.readInt();
         }
      }
      
      Board initial = new Board(tiles);

      // System.out.println(initial.dimension());
      System.out.println(initial.hamming());
      System.out.println(initial.manhattan());
      // System.out.println(initial.isGoal());
      // for(Board b: initial.neighbors()) {
      //    System.out.println(b.toString());
      // }
      // System.out.println(initial.toString());
      
   }

}