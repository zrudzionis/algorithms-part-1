import java.util.ArrayList;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.MinPQ;

public class Solver implements Iterable<Board> {
    private Board board;
    private int moves = -1;
    private MinPQ<Node> minpq = new MinPQ<Node>();
    private boolean isGameSolvable;
    private ArrayList<Board> gameMoves = new ArrayList<Board>();

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
        return gameMoves.size();
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return this;
    }

    public Iterator<Board> iterator() {
        return new SolutionIterator();
    }

    private class SolutionIterator implements Iterator<Board> {
        private int idx = 0;

        public boolean hasNext() {
            return idx + 1 < gameMoves.size();
        }

        public Board next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Iterator does not have next");
            } else {
                Board board = gameMoves.get(idx);
                idx += 1;
                return board;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    private void solve() {

    }

    private class Node implements Comparable<Node> {
        public Board parent = null;
        public Board board;
        public int moves = 0;

        public Node(Board board) {
            this.board = board;
        }

        public Node(Board parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
        }

        @Override
        public int compareTo(Node other) {
            int selfd = board.manhattan() + moves;
            int otherd = other.board.manhattan() + moves;
            return selfd - otherd;
        }
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}