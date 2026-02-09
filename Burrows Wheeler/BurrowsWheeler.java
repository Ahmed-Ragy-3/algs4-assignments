import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// import edu.princeton.cs.algs4.Huffman;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

@SuppressWarnings("unused")
public class BurrowsWheeler {

	private static final int R = 256;

	private static String sort(String str) {
		int n = str.length();
		char[] srt = new char[n];

		int[] cnt = new int[Math.max(R, n)];

		for (int i = 0; i < n; i++)	cnt[str.charAt(i)]++;
		for (int i = 1; i < R; i++)	cnt[i] += cnt[i - 1];
		for (int i = 0; i < n; i++) 	srt[--cnt[str.charAt(i)]] = str.charAt(i);

		return new String(srt);
	}

	// apply Burrows-Wheeler transform,
	// reading from standard input and writing to standard output
	public static void transform() {
		String s = BinaryStdIn.readString();
		int n = s.length();

		CircularSuffixArray suffixArray = new CircularSuffixArray(s);

		int first = 0;
		for (int i = 0; i < n; i++) {
			if (suffixArray.index(i) == 0) {
				first = i;
				break;
			}
		}
		
		BinaryStdOut.write(first);

		for (int i = 0; i < n; i++) {
			int j = suffixArray.index(i);
			BinaryStdOut.write(s.charAt((j == 0 ? n : j) - 1));
		}
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler inverse transform,
	// reading from standard input and writing to standard output
	public static void inverseTransform() {
		int first = BinaryStdIn.readInt();
		String t = BinaryStdIn.readString();
		int n = t.length();

		String srt = sort(t);
		int[] next = new int[n];

		// Constructing next[] array
		HashMap<Character, Queue<Integer>> map = new HashMap<>();
		for (int i = 0; i < n; i++) {
			map.putIfAbsent(t.charAt(i), new LinkedList<>());
			map.get(t.charAt(i)).add(i);
		}

		for (int i = 0; i < n; i++) {
			next[i] = map.get(srt.charAt(i)).poll();
		}
		System.out.println("next[]: " + Arrays.toString(next));

		// Original string construction from next[] and srt
		int cur = first;
		for (int i = 0; i < n; i++) {
			BinaryStdOut.write(srt.charAt(cur));
			cur = next[cur];
		}

		BinaryStdOut.close();
	}

	// if args[0] is "-", apply Burrows-Wheeler transform
	// if args[0] is "+", apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
		// assert args.length == 1;
		if (args[0].equals("-"))
			BurrowsWheeler.transform();

		else if (args[0].equals("+"))
			BurrowsWheeler.inverseTransform();

		else
			throw new IllegalArgumentException("Invalid argument");
	}
}
