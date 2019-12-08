import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.HashMap;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private final Board board;
    private final ArrayList<Board> gameMoves = new ArrayList<Board>();

    private boolean solutionAttempted = false;
    private boolean isGameSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial cannot be null");
        }
        this.board = initial;
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isGameSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!isGameSolvable) {
            return -1;
        }
        return gameMoves.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new SolutionIterable();
    }

    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }	
    }

    private class SolutionIterator implements Iterator<Board> {
        private int idx = 0;

        public boolean hasNext() {
            return idx < gameMoves.size();
        }

        public Board next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Iterator does not have next");
            } else {
                Board nextBoard = gameMoves.get(idx);
                idx += 1;
                return nextBoard;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    private void solve() {
        if (solutionAttempted) {
            return;
        }

        solutionAttempted = true;

        MinPQ<Node> openSet = new MinPQ<Node>();
        openSet.insert(new Node(this.board));

        // heuristic + number of moves made to get to the search node
        HashMap<String, Integer> distance = new HashMap<String, Integer>();
        distance.put(this.board.toString(), 0);

        HashMap<String, Integer> cost = new HashMap<String, Integer>();
        distance.put(this.board.toString(), 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.delMin();
            Board currentBoard = current.board;

            if (currentBoard.isGoal()) {
                isGameSolvable = true;
                saveSolution(current);
                return;
            }

            for (Board neighbour : currentBoard.neighbors()) {
                int newDistance = distance.get(currentBoard.toString()) + 1;
                int oldDistance = distance.getOrDefault(neighbour, Integer.MAX_VALUE);
                if (newDistance < oldDistance) {
                    distance.put(neighbour.toString(), newDistance);
                    Node node = new Node(current, neighbour, newDistance);
                    int oldCost = cost.getOrDefault(neighbour, Integer.MAX_VALUE);
                    int newCost = node.cost();
                    if (newCost < oldCost) {
                        openSet.insert(node);
                    }
                }
            }
        }

    }

    private void saveSolution(Node goalNode) {
        ArrayList<Board> moves = new ArrayList<Board>();
        Node node = goalNode;
        while (node != null) {
            moves.add(node.board);
            node = node.parent;
        }

        for (int i = moves.size() - 1; i > -1; i--) {
            gameMoves.add(moves.get(i));
        }
    }

    private class Node implements Comparable<Node> {
        public Node parent = null;
        public Board board;
        public int moves = 0;

        public Node(Board board) {
            this.board = board;
        }

        public Node(Node parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
        }

        public int cost() {
            return board.manhattan() + moves;
        }

        @Override
        public int compareTo(Node other) {
            return cost() - other.cost();
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}