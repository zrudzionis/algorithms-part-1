import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private Board board;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial cannot be null");
        }
        this.board = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {

    }

    // min number of moves to solve initial board
    public int moves() {

    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {

    }

    // test client (see below)
    public static void main(String[] args) {

    }
}