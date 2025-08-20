public class Trie {
	private static final int R = 26; // Only A-Z
	private final TrieNode root;

	public static class TrieNode {
		private final TrieNode[] children;
		private boolean isEnd;
		
		public TrieNode() {
			children = new TrieNode[R];
			isEnd = false;
		}
	}
	
	public Trie() {
		root = new TrieNode();
	}

	public TrieNode getRoot() {
		return root;
	}

	// Convert character A-Z to index 0-25
	private int index(char c) {
		return c - 'A';
	}

	// Insert a word into the trie
	public void insert(String word) {
		TrieNode node = root;
		for (char c : word.toCharArray()) {
			int idx = index(c);
			if (node.children[idx] == null) {
				node.children[idx] = new TrieNode();
			}
			node = node.children[idx];
		}
		node.isEnd = true;
	}

	// Check if word exists in trie
	public boolean contains(String word) {
		TrieNode node = root;
		for (char c : word.toCharArray()) {
			int idx = index(c);
			if (node.children[idx] == null) {
				return false;
			}
			node = node.children[idx];
		}
		return node.isEnd;
	}

	// Check if node has a child for char c (prefix check helper)
	public boolean containsPrefix(TrieNode node, char c) {
		if (node == null) return false;
		int idx = index(c);
		return node.children[idx] != null;
	}

	// For external calls, you may also want a direct prefix check from root
	public boolean containsPrefix(String prefix) {
		TrieNode node = root;
		for (char c : prefix.toCharArray()) {
			int idx = index(c);
			if (node.children[idx] == null) {
				return false;
			}
			node = node.children[idx];
		}
		return true;
	}

}
