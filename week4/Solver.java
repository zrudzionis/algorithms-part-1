
import java.util.ArrayList;
import java.util.List;

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
        if (!isGameSolvable) {
            return null;
        }
        List<Board> gameMovesCopy = new ArrayList<>(gameMoves);
        return gameMovesCopy;
    }

    private void solve() {
        if (solutionAttempted) {
            return;
        }

        solutionAttempted = true;

        MinPQ<Node> openSet = new MinPQ<Node>();
        openSet.insert(new Node(this.board));

        MinPQ<Node> twinOpenSet = new MinPQ<Node>();
        twinOpenSet.insert(new Node(this.board.twin()));

        Node goalNode = null;
        Node twinGoalNode = null;

        while (goalNode == null && twinGoalNode == null && (!openSet.isEmpty() || !openSet.isEmpty())) {
            goalNode = nextIteration(openSet);
            twinGoalNode = nextIteration(twinOpenSet);
        }

        if (goalNode != null) {
            isGameSolvable = true;
            saveSolution(goalNode);
        }
    }

    private Node nextIteration(MinPQ<Node> openSet) {
        if (openSet.isEmpty()) {
            return null;
        }

        Node current = openSet.delMin();
        Board currentBoard = current.board;

        if (currentBoard.isGoal()) {
            return current;
        }

        for (Board neighbour : currentBoard.neighbors()) {
            if (current.parent == null || !current.parent.board.equals(neighbour)) {
                Node node = new Node(current, neighbour, current.moves + 1);
                openSet.insert(node);
            }
        }

        return null;
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
        private final int manhattan;

        public Node(Board board) {
            this.board = board;
            this.manhattan = board.manhattan();
        }

        public Node(Node parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
            this.manhattan = board.manhattan();
        }

        public int cost() {
            return manhattan + moves;
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