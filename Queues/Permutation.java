//import java.lang.*;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
   public static void main(String[] args) {
      RandomizedQueue<String> rand_queue = new RandomizedQueue<>();
      int k = Integer.parseInt(args[0]);

      while (!StdIn.isEmpty()) {
         String str = StdIn.readString();
         rand_queue.enqueue(str);
     }

      while (k-- > 0) {
         StdOut.println(rand_queue.dequeue());
      }
   }
}