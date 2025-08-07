import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
   
   private LineSegment[] lines;
   private int size;

   public FastCollinearPoints(Point[] points) {
      // finds all line segments containing 4 or more points
      this.size = 0;
      // check
      if(points == null) exc();
      for(Point pt : points) {
         if(pt == null) exc();
      }
      
      int n = points.length;
      Point[] copy = points.clone();
      Arrays.sort(copy);
      for(int i = 0; i < n-1; i++) {
         if(copy[i].compareTo(copy[i + 1]) == 0) exc();
      }
      
      lines = new LineSegment[n * n];
      Point[] fixed = copy.clone();
      Point p;

      for(int i = 0; i < n; i++) {    // for each point p
         p = fixed[i];
         Arrays.sort(copy);
         Arrays.sort(copy, p.slopeOrder());
         
         // sliding window
         int index = 0;
         int offset;
         double slope;

         while(index < n - 2) {

            offset = 1;
            slope = p.slopeTo(copy[index]);
            while(index + offset < n && slope == p.slopeTo(copy[index + offset])) {
               offset++;
            }

            offset--;
            if(offset >= 2) {

               if(p.compareTo(copy[index]) < 0 && p.compareTo(copy[index + offset]) < 0) {
                  // if only p less than both points
                  lines[size++] = new LineSegment(p, copy[index + offset]);
               }
               index += offset;
            }
            index++;
         }
      }     
   }

   private void exc() {
      throw new IllegalArgumentException();
   }

   public int numberOfSegments() {
      return this.size;
  }

  public LineSegment[] segments() {
      return Arrays.copyOf(lines, size);
  }

   public static void main(String[] args) {

      // read the n points from a file
      In in = new In(args[0]);
      int n = in.readInt();
      Point[] points = new Point[n];
      for(int i = 0; i < n; i++) {
         int x = in.readInt();
         int y = in.readInt();
         points[i] = new Point(x, y);
      }

      // draw the points
      StdDraw.enableDoubleBuffering();
      StdDraw.setXscale(0, 32768);
      StdDraw.setYscale(0, 32768);
      for(Point p : points) {
         p.draw();
      }
      StdDraw.show();

      // print and draw the line segments
      FastCollinearPoints collinear = new FastCollinearPoints(points);
      for(LineSegment segment : collinear.segments()) {
         StdOut.println(segment);
         segment.draw();
      }
      StdDraw.show();
   }
}