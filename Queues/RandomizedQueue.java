// import java.lang.*;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
   private Item[] queue;
   private int size;

   // construct an empty randomized queue
   @SuppressWarnings("unchecked")
   public RandomizedQueue() {
      queue = (Item[]) new Object[50];
      this.size = 0;
   }

   @SuppressWarnings("unchecked")
   private void resize() {
      if (size == queue.length) {
         // increase size to double
         Item[] helper = (Item[]) new Object[size];

         for (int i = 0; i < size; i++) {
            helper[i] = queue[i];
         }

         queue = (Item[]) new Object[size * 2];
         for (int i = 0; i < size; i++) {
            queue[i] = helper[i];
         }

      } else if (size == queue.length / 4) {
         // shrink size to half
         Item[] helper = (Item[]) new Object[size];

         for (int i = 0; i < size; i++) {
            helper[i] = queue[i];
         }

         queue = (Item[]) new Object[queue.length / 2];
         for (int i = 0; i < size; i++) {
            queue[i] = helper[i];
         }

      }
   }

   // is the randomized queue empty?
   public boolean isEmpty() {
      return this.size == 0;
   }

   // return the number of items on the randomized queue
   public int size() {
      return this.size;
   }

   // add the item
   public void enqueue(Item item) {
      if (item == null)
         throw new IllegalArgumentException();

      queue[size] = item;
      size++;
      resize();
   }

   // remove and return a random item
   public Item dequeue() {
      if (this.isEmpty())
         throw new NoSuchElementException();

      resize();
      if (this.size == 1) {
         size--;
         return queue[size];
      }
      int randIndex = StdRandom.uniformInt(0, size - 1);
      Item element = queue[randIndex];
      queue[randIndex] = queue[--size];
      queue[size] = null;
      return element;
   }

   // return a random item (but do not remove it)
   public Item sample() {
      if (this.isEmpty())
         throw new NoSuchElementException();

      if (this.size == 1) {
         return queue[0];
      }
      return queue[StdRandom.uniformInt(0, size - 1)];
   }
   
   // return an independent iterator over items in random order
   public Iterator<Item> iterator() {
      StdRandom.shuffle(this.queue, 0, size);

      Iterator<Item> iter = new Iterator<Item>() {
         private int cur = 0;
         
         public boolean hasNext() {
            return (cur != size);
         }
         
         public Item next() {
            if (!hasNext()) {
               throw new NoSuchElementException();
            }
            Item item = queue[cur++];
            return item;
         }
         
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
      return iter;
   }

   // unit testing (required)
   public static void main(String[] args) {
      RandomizedQueue<Integer> randQ = new RandomizedQueue<>();
      int n = 10;
      StdOut.println(randQ.isEmpty());
      for (int i = 1; i <= n; i++) {
         randQ.enqueue(i);
      }

      for (int i = 0; i < 20; i++) {
         StdOut.print(randQ.sample() + " ");
         StdOut.println(randQ.dequeue());
      }
      StdOut.println("Size = " + randQ.size());
      for (int rem : randQ) {
         StdOut.print(rem + " ");
      }
   }
}