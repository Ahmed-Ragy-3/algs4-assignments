import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


class Node implements Comparable<Node> {
   Board current;
   Node prev;
   int moves_made;

   public Node(Board current, Node prev, int moves_made) {
      this.current = current;
      this.prev = prev;
      this.moves_made = moves_made;
   }

   @Override
   public int compareTo(Node node) {
      return (this.moves_made + this.current.manhattan()) - (node.moves_made + node.current.manhattan());
   }
}

public class Solver {

   // find a solution to the initial board (using the A* algorithm)
   private Node goal = null;
   private boolean solvable = false;

   public Solver(Board initial) {
      if(initial == null)
         throw new IllegalArgumentException("initial board is null");


      MinPQ<Node> pq = new MinPQ<>();
      MinPQ<Node> pqTwin = new MinPQ<>();

      pq.insert(new Node(initial, null, 0));
      pqTwin.insert(new Node(initial.twin(), null, 0));

      while (!pq.isEmpty()) {
         Node temp = pq.delMin();
         if(temp.current.isGoal()) {
            goal = temp;
            solvable = true;
            break;
         }

         for(Board neighbor : temp.current.neighbors()) {
            if(temp.prev != null && temp.prev.current.equals(neighbor)) continue;    // optimization
            pq.insert(new Node(neighbor, temp, temp.moves_made + 1));
         }

         // twin
         Node tempTwin = pqTwin.delMin();
         if(tempTwin.current.isGoal()) {
            goal = tempTwin;
            solvable = false;
            break;
         }

         for(Board neighbor : tempTwin.current.neighbors()) {
            if(tempTwin.prev != null && tempTwin.prev.current.equals(neighbor)) continue;    // optimization
            pqTwin.insert(new Node(neighbor, tempTwin, tempTwin.moves_made + 1));
         }
      }
   }

   // is the initial board solvable? (see below)
   public boolean isSolvable() {
      return solvable;
   }

   // min number of moves to solve initial board; -1 if unsolvable
   public int moves() {
      if(!isSolvable()) return -1;
      return goal.moves_made;
   }

   // sequence of boards in a shortest solution; null if unsolvable
   public Iterable<Board> solution() {
      if(!isSolvable()) return null;

      Stack<Node> stack = new Stack<>();
      Node temp = goal;
      while (temp != null) {
         stack.push(temp);
         temp = temp.prev;
      }
      return new Iterable<Board>() {
         @Override
         public Iterator<Board> iterator() {
            return new Iterator<Board>() {
               @Override
               public boolean hasNext() {
                  return !stack.isEmpty();
               }

               @Override
               public Board next() {
                  return stack.pop().current;
               }
            };
         }
      };
   }

   // test client (see below) 
   public static void main(String[] args) {
      // create initial board from file
      In in = new In(args[0]);
      
      int n = in.readInt();
      int[][] tiles = new int[n][n];

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            tiles[i][j] = in.readInt();
         }
      }
      Board initial = new Board(tiles);

      // solve the puzzle
      Solver solver = new Solver(initial);

      // print solution to standard output
      if (!solver.isSolvable()) {
         StdOut.println("No solution possible");

      } else {
         StdOut.println("Minimum number of moves = " + solver.moves());
         for (Board board : solver.solution())
            StdOut.println(board);
      }
   }

}