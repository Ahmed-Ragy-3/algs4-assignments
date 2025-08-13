import java.awt.Color;
import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
   private static double BORDER_ENERGY = 1000.0;
   private static int INF = Integer.MAX_VALUE;
   private Picture pic;
   private int W, H;

   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      if (picture == null)
         throw new IllegalArgumentException();
      this.pic = picture;
      this.W = picture.width();
      this.H = picture.height();
   }

   // current picture
   public Picture picture() {
      return new Picture(pic);
   }

   // width of current picture
   public int width() {
      return W;
   }

   // height of current picture
   public int height() {
      return H;
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
      if (x >= W || y >= H || x < 0 || y < 0)
         throw new IllegalArgumentException();
      else if (x + 1 == W || x == 0 || y + 1 == H || y == 0)
         return BORDER_ENERGY;

      Color left = pic.get(x - 1, y);
      Color right = pic.get(x + 1, y);
      Color up = pic.get(x, y - 1);
      Color down = pic.get(x, y + 1);

      int rx = Math.abs(left.getRed() - right.getRed());
      int gx = Math.abs(left.getGreen() - right.getGreen());
      int bx = Math.abs(left.getBlue() - right.getBlue());

      int ry = Math.abs(up.getRed() - down.getRed());
      int gy = Math.abs(up.getGreen() - down.getGreen());
      int by = Math.abs(up.getBlue() - down.getBlue());

      int dxSq = (rx * rx) + (gx * gx) + (bx * bx);
      int dySq = (ry * ry) + (gy * gy) + (by * by);

      return Math.sqrt((double) (dxSq + dySq));
   }

   private int val(int r, int c) {
      return (r * W + c);
   }

   private Iterable<Integer> adjVertical(int r, int c) {
      List<Integer> list = new LinkedList<>();
      // int[] idxs = indices(idx);
      // int r = idxs[0], c = idxs[1];
      if (r == H - 1) {
         list.add(W * H);
         return list;
      }

      list.add(val(r + 1, c));
      if (c > 0)
         list.add(val(r + 1, c - 1));
      if (c + 1 < W)
         list.add(val(r + 1, c + 1));

      return list;
   }

   private Iterable<Integer> adjhorizontal(int r, int c) {
      List<Integer> list = new LinkedList<>();
      // int[] idxs = indices(idx);
      // int r = idxs[0], c = idxs[1];
      if (c == W - 1) {
         list.add(W * H);
         return list;
      }

      list.add(val(r, c + 1));
      if (r > 0)
         list.add(val(r - 1, c + 1));
      if (r + 1 < H)
         list.add(val(r + 1, c + 1));

      return list;
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
      int[] seam = new int[W];

      // add extra node for target
      final int target = W * H;
      double[] dis = new double[target + 1];
      int[] par = new int[dis.length];

      for (int i = 0; i <= target; i++) {
         dis[i] = INF;
      }

      for (int r = 0; r < H; r++) {
         dis[val(r, 0)] = 0;
         par[val(r, 0)] = -1;
      }

      for (int c = 0; c < W; c++) {
         for (int r = 0; r < H; r++) {
            int u = val(r, c);
            double w = energy(c, r);
            for (int v : adjhorizontal(r, c)) {
               // relax
               if (dis[u] + w < dis[v]) {
                  dis[v] = dis[u] + w;
                  par[v] = u;
               }
            }
         }
      }

      int u = par[target];
      int c = W - 1;
      while (u != -1 && c >= 0) {
         seam[c--] = u / W; // convert to row index
         u = par[u];
      }

      return seam;
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {
      int[] seam = new int[H];

      // add extra node for target
      final int target = W * H;
      double[] dis = new double[target + 1];
      int[] par = new int[dis.length];

      for (int c = 0; c < W; c++) {
         dis[c] = 0;
         par[c] = -1;
      }

      for (int i = W; i <= target; i++) {
         dis[i] = INF;
      }

      for (int r = 0; r < H; r++) {
         for (int c = 0; c < W; c++) {
            int u = val(r, c);
            double w = energy(c, r);
            for (int v : adjVertical(r, c)) {
               // relax
               if (dis[u] + w < dis[v]) {
                  dis[v] = dis[u] + w;
                  par[v] = u;
               }
            }
         }
      }

      int u = par[target];
      int r = H - 1;
      while (u != -1 && r >= 0) {
         seam[r--] = u % W; // convert to column index
         u = par[u];
      }

      return seam;
   }

   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] seam) {
      if (seam == null || seam.length != W || H <= 1)
         throw new IllegalArgumentException();

      Picture newPic = new Picture(W, H - 1);
      for (int c = 0; c < W; c++) {
         if (seam[c] < 0 || seam[c] >= H)
            throw new IllegalArgumentException();

         else if (c > 0 && Math.abs(seam[c] - seam[c - 1]) > 1)
            throw new IllegalArgumentException();

         int r;
         for (r = 0; r < seam[c]; r++) {
            newPic.set(c, r, pic.get(c, r));
         }
         while (++r < H) {
            newPic.set(c, r - 1, pic.get(c, r));
         }
      }

      this.pic = newPic;
      H--;
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
      if (seam == null || seam.length != H || W <= 1)
         throw new IllegalArgumentException();

      Picture newPic = new Picture(W - 1, H);
      for (int r = 0; r < H; r++) {
         if (seam[r] < 0 || seam[r] >= W)
            throw new IllegalArgumentException();

         else if (r > 0 && Math.abs(seam[r] - seam[r - 1]) > 1)
            throw new IllegalArgumentException();

         int c;
         for (c = 0; c < seam[r]; c++) {
            newPic.set(c, r, pic.get(c, r));
         }
         while (++c < W) {
            newPic.set(c - 1, r, pic.get(c, r));
         }
      }

      this.pic = newPic;
      W--;
   }

   // unit testing (optional)
   public static void main(String[] args) {
      SeamCarver sc = new SeamCarver(new Picture("test.png"));

      sc.pic.show();
      for (int i = 0; i < 400; i++) {
         sc.removeHorizontalSeam((sc.findHorizontalSeam()));
      }
      sc.pic.show();
   }
}