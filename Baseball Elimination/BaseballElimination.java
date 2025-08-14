import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {

	private final static int INF = Integer.MAX_VALUE;
	private final int n;
	private final int[] w, l, rem; // wins, loss, remaining
	private final int[][] grem;
	private final String[] teamNames;
	private final HashMap<String, Integer> teamsMap;
	private final HashMap<Integer, LinkedList<String>> subset;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		if (filename == null)
			throw new IllegalArgumentException();

		String[] lines = new In(filename).readAllLines();
		this.n = Integer.parseInt(lines[0]);
		this.w = new int[n];
		this.l = new int[n];
		this.rem = new int[n];
		this.grem = new int[n][n];
		this.teamNames = new String[n];
		this.teamsMap = new HashMap<>(n);
		this.subset = new HashMap<>(n);

		for (int i = 0; i < n; i++) {
			String[] cols = lines[i + 1].trim().split("\\s+");

			this.teamNames[i] = cols[0];
			this.teamsMap.put(cols[0], i);
			this.w[i] = Integer.parseInt(cols[1]);
			this.l[i] = Integer.parseInt(cols[2]);
			this.rem[i] = Integer.parseInt(cols[3]);

			for (int j = 0; j < n; j++) {
				this.grem[i][j] = Integer.parseInt(cols[4 + j]);
			}
		}
	}

	private int getTeam(String team) {
		if (!teamsMap.containsKey(team))
			throw new IllegalArgumentException("Invalid team name");
		return teamsMap.get(team);
	}

	// number of teams
	public int numberOfTeams() {
		return n;
	}

	// all teams
	public Iterable<String> teams() {
		return teamsMap.keySet();
	}

	// number of wins for given team
	public int wins(String team) {
		return w[getTeam(team)];
	}

	// number of losses for given team
	public int losses(String team) {
		return l[getTeam(team)];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		return rem[getTeam(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		return grem[getTeam(team1)][getTeam(team2)];
	}

	private void solve(int elim) {
		int maxWin = w[elim] + rem[elim];
		LinkedList<String> R = new LinkedList<>();

		// Trivial elimination
		for (int i = 0; i < n; i++) {
			if (w[i] > maxWin) {
				R.add(teamNames[i]);
				subset.put(elim, R);
				return;
			}
		}

		// Non Trivial elimination
		// [1] [(n-1)C2] [n - 1] [1]
		int V = (n - 1) * (n - 2) / 2 + (n - 1) + 2;
		int s = 0, t = V - 1;
		int total = 0;

		FlowNetwork network = new FlowNetwork(V);
		int start1 = 1, start2 = t - (n - 1);
		for (int i = 0; i < n; i++) {
			if (i == elim) continue;

			int ti = start2 + (i > elim ? i - 1 : i);
			for (int j = i + 1; j < n; j++) {
				if (j == elim) continue;

				int tj = start2 + (j > elim ? j - 1 : j);

				network.addEdge(new FlowEdge(s, start1, grem[i][j]));
				total += grem[i][j];

				network.addEdge(new FlowEdge(start1, ti, INF));
				network.addEdge(new FlowEdge(start1, tj, INF));
				start1++;
			}

			network.addEdge(new FlowEdge(ti, t, maxWin - w[i]));
		}

		FordFulkerson maxFlow = new FordFulkerson(network, s, t);

		if (maxFlow.value() != total) {
			start2 = t - (n - 1);
			for (int i = 0; i < n; i++) {
				if (i == elim) continue;
				if (maxFlow.inCut(start2++)) {
					R.add(teamNames[i]);
				}
			}
		}

		subset.put(elim, R);
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		int elim = getTeam(team);
		if (!subset.containsKey(elim))
			solve(elim);
		return !subset.get(elim).isEmpty();
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		int elim = getTeam(team);
		if (!subset.containsKey(elim))
			solve(elim);
		LinkedList<String> ret = subset.get(elim);
		return ret.isEmpty() ? null : ret;
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}