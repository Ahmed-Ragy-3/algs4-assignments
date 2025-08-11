import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private WordNet wordnet;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		check(wordnet);
		this.wordnet = wordnet;
	}

	private void check(Object... args) {
		if (args == null)
			throw new IllegalArgumentException();
		for (Object arg : args) {
			if (arg == null)
				throw new IllegalArgumentException();
		}
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		check((Object[]) nouns);
		String out = "";
		int maxDis = -1;
		for (int i = 0; i < nouns.length; i++) {
			int sum = 0;
			for (int j = 0; j < nouns.length; j++) {
				if (i == j)
					continue;
				sum += wordnet.distance(nouns[i], nouns[j]);
			}

			if (sum > maxDis) {
				maxDis = sum;
				out = nouns[i];
			}
		}

		return out;
	}

	// see test client below
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}