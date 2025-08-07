import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
   private final LineSegment[] lines;
   private int size = 0;
   
   public BruteCollinearPoints(Point[] points) {
      // check
      if(points == null) exc();
      for(Point p : points) {
         if(p == null) exc();
      }
      
      int n = points.length;
      Point[] copy = points.clone();
      Arrays.sort(copy);
      for(int i = 0; i < n-1; i++) {
         if(copy[i].compareTo(copy[i + 1]) == 0)
            exc();
      }
      
      lines = new LineSegment[n * n];
      
      for(int p = 0; p < n - 3; p++) {
         for(int q = p + 1; q < n - 2; q++) {
            for(int r = q + 1; r < n - 1; r++) {
               for(int s = r + 1; s < n; s++) {

                  double slopePQ = copy[p].slopeTo(copy[q]);
                  double slopePR = copy[p].slopeTo(copy[r]);
                  double slopePS = copy[p].slopeTo(copy[s]);
                  if(equalSlopes(slopePQ, slopePR) && equalSlopes(slopePQ, slopePS)) {
                     lines[size++] = new LineSegment(copy[p], copy[s]);
                  }
               }
            }
         }
      }
      
   }


   private void exc() {
      throw new IllegalArgumentException();
   }

   private boolean equalSlopes(double s1, double s2) {
      return Double.compare(s1, s2) == 0;
   }
   
   public int numberOfSegments() {
      return size;
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
      BruteCollinearPoints collinear = new BruteCollinearPoints(points);
      for(LineSegment segment : collinear.segments()) {
         StdOut.println(segment);
         segment.draw();
      }
      StdDraw.show();
   }

}
