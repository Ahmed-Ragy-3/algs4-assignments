import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.HexDump;

import java.util.LinkedList;

import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;
import edu.princeton.cs.algs4.BinaryStdIn;

@SuppressWarnings("unused")
public class MoveToFront {

	private static void fillSequence(LinkedList<Character> sequence) {
		for (int i = 0; i < 256; i++) {
			// ith extended ASCII character
			sequence.addLast((char) i);
		}
	}

	// apply move-to-front encoding, reading from standard input and
	// writing to abra.txt
	public static void encode() {
		LinkedList<Character> seq = new LinkedList<>();
		fillSequence(seq);

		BinaryOut out = new BinaryOut("abra.txt");

		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			int idx = seq.indexOf(c);
			out.write(idx, 8);
			seq.remove(idx);
			seq.addFirst(c);
		}
		out.close();
	}

	// apply move-to-front decoding, reading from standard input and
	// writing to abra.txt
	public static void decode() {
		LinkedList<Character> seq = new LinkedList<>();
		fillSequence(seq);

		BinaryOut out = new BinaryOut("abra.txt");

		while (!BinaryStdIn.isEmpty()) {
			int idx = BinaryStdIn.readChar();
			char c = seq.get(idx);
			out.write(c, 8);
			seq.remove(idx);
			seq.addFirst(c);
		}
		out.close();
	}

	// if args[0] is "-", apply move-to-front encoding
	// if args[0] is "+", apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			MoveToFront.encode();
		
		else if (args[0].equals("+"))
			MoveToFront.decode();
		
		else
			throw new IllegalArgumentException("Invalid argument");
	}
}
