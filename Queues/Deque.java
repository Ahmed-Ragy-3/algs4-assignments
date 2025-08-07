import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

   private class Node<T> {
      T data;
      Node<T> prev;
      Node<T> next;

      private Node(T d, Node<T> p, Node<T> n) {
         this.data = d;
         this.next = n;
         this.prev = p;
      }
   }

   // attributes
   private Node<Item> head;
   private Node<Item> tail;
   private int sz;

   // construct an empty deque
   public Deque() {
      this.head = null;
      this.tail = head;
      this.sz = 0;
   }

   // is the deque empty?
   public boolean isEmpty() {
      return (this.sz == 0);
   }

   // return the number of items on the deque
   public int size() {
      return this.sz;
   }

   // add the item to the front
   public void addFirst(Item item) {
      if (item == null)
         throw new IllegalArgumentException();

      if (this.isEmpty()) {
         Node<Item> newNode = new Node<Item>(item, null, null);
         head = newNode;
         tail = newNode;

      } else {
         Node<Item> newNode = new Node<Item>(item, null, head);
         head.prev = newNode;
         head = newNode;
      }
      sz++;
   }

   // add the item to the back
   public void addLast(Item item) {
      if (item == null)
         throw new IllegalArgumentException();

      if (this.isEmpty()) {
         Node<Item> newNode = new Node<Item>(item, null, null);
         head = newNode;
         tail = newNode;

      } else {
         Node<Item> newNode = new Node<Item>(item, tail, null);
         tail.next = newNode;
         tail = newNode;
      }
      sz++;
   }

   // remove and return the item from the front
   public Item removeFirst() {
      if (this.isEmpty())
         throw new NoSuchElementException();

      Item output = head.data;
      if(this.sz == 1) {
         head = null;
         tail = null;
      }else {
         head = head.next;
         head.prev = null;
      }
      sz--;
      return output;
   }

   // remove and return the item from the back
   public Item removeLast() {
      if (this.isEmpty())
         throw new NoSuchElementException();

      Item output = tail.data;
      if(this.sz == 1) {
         head = null;
         tail = null;
      }else {
         tail = tail.prev;
         tail.next = null;
      }
      sz--;
      return output;
   }

   // return an iterator over items in order from front to back
   public Iterator<Item> iterator() {
      Iterator<Item> iter = new Iterator<Item>() {
         private Node<Item> cur = head;

         public boolean hasNext() {
            return (cur != null);
         }

         public Item next() {
            if (!hasNext()) {
               throw new NoSuchElementException();
            }
            Item item = cur.data;
            cur = cur.next;
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
      int n = 11;
      Deque<Integer> deq = new Deque<>();
      StdOut.println(deq.isEmpty());
      for (int i = 1; i <= n; i++) {
         deq.addLast(i);
         deq.addFirst(i);
      }

      StdOut.println(deq.removeFirst());
      StdOut.println(deq.removeLast());

      StdOut.println("Size = " + deq.size());

      for (int element : deq) {
         StdOut.print(element);
         StdOut.print(' ');
      }

   }

}