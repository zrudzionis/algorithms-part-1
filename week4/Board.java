import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] tiles;
    private final int n;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;
    private String str = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = Arrays.stream(tiles)
                .map(a -> Arrays.copyOf(a, a.length))
                .toArray(int[][]::new);

        this.n = tiles.length;
    }

    // string representation of this board
    public String toString() {
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(n));
        stringBuilder.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(Integer.toString(tiles[i][j]));
            }
            stringBuilder.append("\n");
        }
        str = stringBuilder.toString();
        return str;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingDistance >= 0) {
            return hammingDistance;
        }

        int h = 0;
        int last = n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = tiles[i][j];
                if (i == last && j == last) {
                    h += v != 0 ? 1 : 0;
                } else {
                    h += v != i*n + j + 1 ? 1 : 0;
                }

            }
        }
        hammingDistance = h;
        return h;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance >= 0) {
            return manhattanDistance;
        }

        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = tiles[i][j];
                int ei, ej;
                if (v == 0) {
                    ei = n - 1;
                    ej = n - 1;
                } else {
                    ei = (v - 1) / n;
                    ej = (v - 1) % n;
                }
                m += Math.abs(i - ei) + Math.abs(j - ej);
            }
        }
        manhattanDistance = m;
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        return this.toString().equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zr = 0;
        int zc = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    zr = i;
                    zc = j;
                    break;
                }
            }
        }

        int[] rows = {0, 0, 1, -1};
        int[] cols = {1, -1, 0, 0};
        int idx = 0;
        Stack<Board> boards =  new Stack<Board>();
        while (idx < 4) {
            int nr = zr + rows[idx];
            int nc = zc + cols[idx];
            if (!(nr >= n || nr < 0 || nc >= n || nc < 0)) {
                Board nextBoard = getBoardAfterSwap(zr, zc, nr, nc);
                boards.push(nextBoard);
            }
            idx += 1;
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int a = StdRandom.uniform(n*n - 1) + 1; // excluding blank tile
        int b = 0;
        while (b == 0 || b == a) {
            b = StdRandom.uniform(n*n - 1) + 1;
        }
        int r1 = 0, c1 = 0, r2 = 0, c2 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = tiles[i][j];
                if (v == a) {
                    r1 = i;
                    c1 = j;
                }
                if (v == b) {
                    r2 = i;
                    c2 = j;
                }
            }
        }
        return getBoardAfterSwap(r1, c1, r2, c2);
    }

    private Board getBoardAfterSwap(int r1, int c1, int r2, int c2) {
        swap(r1, c1, r2, c2);
        Board board = new Board(tiles);
        swap(r1, c1, r2, c2);
        return board;
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int tmp = tiles[r2][c2];
        tiles[r2][c2] = tiles[r1][c1];
        tiles[r1][c1] = tmp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        StdOut.println("Board.main");
    }
}
