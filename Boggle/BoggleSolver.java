import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Trie trie;

    private BoggleBoard board;
    private int n, m;
    private boolean[][] vis;
    private HashSet<String> set;
    private StringBuilder strb;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            invalid();

        this.trie = new Trie();
        for (String str : dictionary) {
            if (str == null)
                invalid();
            this.trie.insert(str);
        }
    }

    private void invalid() {
        throw new IllegalArgumentException();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        this.n = board.rows();
        this.m = board.cols();
        this.set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.strb = new StringBuilder();
                this.vis = new boolean[n][m];
                rec(i, j);
            }
        }
        return set;
    }

    private void rec(int r, int c) {
        if (r == n || c == m || r < 0 || c < 0 || vis[r][c]) return;

        vis[r][c] = true;
        char ch = board.getLetter(r, c);
        strb.append(ch);
        if (ch == 'Q') strb.append('U');

        String str = strb.toString();
        if (str.length() >= 3 && trie.contains(str)) {
            set.add(str);

        } else if (!trie.containsPrefix(str)) {
            vis[r][c] = false;
            strb.deleteCharAt(strb.length() - 1);
            if (ch == 'Q')
                strb.deleteCharAt(strb.length() - 1);
            return;
        }

        // vertical
        rec(r - 1, c);
        rec(r + 1, c);

        // horizontal
        rec(r, c - 1);
        rec(r, c + 1);

        // diagonal
        rec(r - 1, c - 1);
        rec(r - 1, c + 1);
        rec(r + 1, c - 1);
        rec(r + 1, c + 1);

        vis[r][c] = false;
        strb.deleteCharAt(strb.length() - 1);
        if (ch == 'Q')
            strb.deleteCharAt(strb.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    // 3,4 1
    // 5 2
    // 6 3
    // 7 5
    // 8+ 11
    public int scoreOf(String word) {
        if (word == null) invalid();
        if (word.length() < 3 || !trie.contains(word))
            return 0;
        return score(word);
    }

    private int score(String word) {
        int len = word.length();

        if (len <= 2)       return 0;
        else if (len <= 4)  return 1;
        else if (len == 5)  return 2;
        else if (len == 6)  return 3;
        else if (len == 7)  return 5;

        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}