import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.HexDump;
import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;
// import edu.princeton.cs.algs4.Huffman;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

@SuppressWarnings("unused")
public class BurrowsWheeler {

	private static String sort(String str) {
		int n = str.length();
		char[] chs = new char[n];
		int alphabet = 256;

		int[] cnt = new int[Math.max(alphabet, n)];

		for (int i = 0; i < n; i++)			cnt[str.charAt(i)]++;
		for (int i = 1; i < alphabet; i++)	cnt[i] += cnt[i - 1];
		for (int i = 0; i < n; i++) 			chs[--cnt[str.charAt(i)]] = str.charAt(i);

		return new String(chs);
	}

	// apply Burrows-Wheeler transform,
	// reading from standard input and writing to standard output
	public static void transform() {
		// String s = BinaryStdIn.readString();
		String s = StdIn.readString();
		int n = s.length();
		CircularSuffixArray suffixArray = new CircularSuffixArray(s);
		for (int i = 0; i < n; i++) {
			int j = suffixArray.index(i);
			BinaryStdOut.write(s.charAt((j == 0 ? n : j) - 1));
			System.out.print(s.charAt((j == 0 ? n : j) - 1));
		}
	}

	// apply Burrows-Wheeler inverse transform,
	// reading from standard input and writing to standard output
	public static void inverseTransform() {
		String L = BinaryStdIn.readString();
		// String F = sort(L)
	}

	// if args[0] is "-", apply Burrows-Wheeler transform
	// if args[0] is "+", apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
		BurrowsWheeler.transform();
		// System.out.println(BurrowsWheeler.sort("ARD!RCAAAABB"));
	}
}
