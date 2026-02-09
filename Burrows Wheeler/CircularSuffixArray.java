import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;

public class CircularSuffixArray {

	private int[] suff;

	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new IllegalArgumentException();
		
		// s += "!";
		int n = s.length();
		int alphabet = 256;

		this.suff = new int[n];		// order of suffixes
		int[] c = new int[n]; 		// equivalence classes
		int[] cnt = new int[Math.max(alphabet, n)];

		// ----- Initial sorting by single characters -----
		for (int i = 0; i < n; i++)			cnt[s.charAt(i)]++;
		for (int i = 1; i < alphabet; i++)	cnt[i] += cnt[i - 1];
		for (int i = 0; i < n; i++) 			suff[--cnt[s.charAt(i)]] = i;

		c[suff[0]] = 0;
		int classes = 0;
		for (int i = 1; i < n; i++) {
			if (s.charAt(suff[i]) != s.charAt(suff[i - 1])) classes++;
			c[suff[i]] = classes;
		}

		int[] pn = new int[n];
		int[] cn = new int[n];

		// ----- Sorting by 2^k length substrings -----
		for (int len = 1; len < n; len <<= 1) {
			// shift left by len
			for (int i = 0; i < n; i++) {
				pn[i] = suff[i] - len;
				if (pn[i] < 0) pn[i] += n;
			}

			Arrays.fill(cnt, 0, classes + 1, 0);
			for (int i = 0; i < n; i++)			cnt[c[pn[i]]]++;
			for (int i = 1; i <= classes; i++)	cnt[i] += cnt[i - 1];
			for (int i = n - 1; i >= 0; i--) 	suff[--cnt[c[pn[i]]]] = pn[i];

			cn[suff[0]] = 0;
			classes = 0;
			for (int i = 1; i < n; i++) {
				int cur1 = c[suff[i]], cur2 = c[(suff[i] + len) % n];
				int prev1 = c[suff[i - 1]], prev2 = c[(suff[i - 1] + len) % n];
				
				if (cur1 != prev1 || cur2 != prev2) classes++;
				cn[suff[i]] = classes;
			}
			System.arraycopy(cn, 0, c, 0, n);
		}
	}

	// length of s
	public int length() {
		return suff.length;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= suff.length)
			throw new IllegalArgumentException();
		return suff[i];
	}

	// unit testing (required)
	public static void main(String[] args) {
		// In in = new In(args[0]); // read from file passed in command line
		String s = StdIn.readString();

		CircularSuffixArray csa = new CircularSuffixArray(s);

		System.out.println("Length: " + csa.length());
		for (int i = 0; i < csa.length(); i++) {
			System.out.println(csa.index(i));
		}
	}

}
