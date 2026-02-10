import java.util.LinkedList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

	private static final int R = 256;

	private static void fillSequence(LinkedList<Integer> sequence) {
		// assert sequence.isEmpty();
		for (int i = 0; i < R; i++) {
			// ith extended ASCII character
			sequence.addLast(i);
		}
	}

	// apply move-to-front encoding, reading from standard input and
	public static void encode() {
		LinkedList<Integer> seq = new LinkedList<>();
		fillSequence(seq);

		// writing to standard output
		while (!BinaryStdIn.isEmpty()) {
			int c = BinaryStdIn.readChar();
			int idx = seq.indexOf(c);
			BinaryStdOut.write(idx, 8);
			seq.remove(idx);
			seq.addFirst(c);
		}

		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and
	public static void decode() {
		LinkedList<Integer> seq = new LinkedList<>();
		fillSequence(seq);

		// writing to standard output
		while (!BinaryStdIn.isEmpty()) {
			int idx = BinaryStdIn.readChar();
			int c = seq.get(idx);
			BinaryStdOut.write(c, 8);
			seq.remove(idx);
			seq.addFirst(c);
		}

		BinaryStdOut.close();
	}

	// if args[0] is "-", apply move-to-front encoding
	// if args[0] is "+", apply move-to-front decoding
	public static void main(String[] args) {
		// assert args.length == 1;
		if (args[0].equals("-"))
			MoveToFront.encode();

		else if (args[0].equals("+"))
			MoveToFront.decode();

		else
			throw new IllegalArgumentException("Invalid argument");
	}
}
