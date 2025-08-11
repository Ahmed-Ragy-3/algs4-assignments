import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private final Digraph graph;
	private static final int INF = Integer.MAX_VALUE;
	private int len;
	private int sca;

	// defensive copy to avoid modifying the original graph
	public SAP(Digraph G) {
		if (G == null)
			throw new IllegalArgumentException();
		this.graph = new Digraph(G);
	}

	// BFS from a set of sources
	private int[] bfs(Iterable<Integer> sources) {
		int[] dist = new int[graph.V()];
		Arrays.fill(dist, INF);

		Queue<Integer> q = new LinkedList<>();
		for (int s : sources) {
			if (s < 0 || s >= graph.V())
				throw new IllegalArgumentException();
			dist[s] = 0;
			q.add(s);
		}

		while (!q.isEmpty()) {
			int u = q.poll();
			for (int v : graph.adj(u)) {
				if (dist[v] == INF) {
					dist[v] = dist[u] + 1;
					q.add(v);
				}
			}
		}
		return dist;
	}

	// compute length & ancestor
	private void computeSAP(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null)
			throw new IllegalArgumentException();

		int[] distV = bfs(v);
		int[] distW = bfs(w);
		len = INF;
		sca = -1;

		for (int i = 0; i < graph.V(); i++) {
			if (distV[i] != INF && distW[i] != INF) {
				int total = distV[i] + distW[i];
				if (total < len) {
					len = total;
					sca = i;
				}
			}
		}

		if (len == INF)
			len = -1;
	}

	// length between two vertices
	public int length(int v, int w) {
		checkVertex(v);
		checkVertex(w);
		computeSAP(List.of(v), List.of(w));
		return len;
	}

	// ancestor between two vertices
	public int ancestor(int v, int w) {
		checkVertex(v);
		checkVertex(w);
		computeSAP(List.of(v), List.of(w));
		return sca;
	}

	// length between any vertex in v and any vertex in w
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		checkVertices(v);
		checkVertices(w);
		computeSAP(v, w);
		return len;
	}

	// ancestor between any vertex in v and any vertex in w
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		checkVertices(v);
		checkVertices(w);
		computeSAP(v, w);
		return sca;
	}

	// input validation
	private void checkVertex(int v) {
		if (v < 0 || v >= graph.V())
			throw new IllegalArgumentException();
	}

	private void checkVertices(Iterable<Integer> vertices) {
		if (vertices == null)
			throw new IllegalArgumentException();
		for (Integer v : vertices) {
			if (v == null || v < 0 || v >= graph.V())
				throw new IllegalArgumentException();
		}
	}

	// unit testing
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);

		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
