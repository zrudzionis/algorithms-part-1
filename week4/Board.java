import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] tiles;
    private final int n;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;
    private Board twinBoard = null;
    private String str = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = cloneTiles(tiles);
        this.n = tiles.length;
    }
    
    private int[][] cloneTiles(int[][] t) {
        return Arrays.stream(t)
                .map(a -> Arrays.copyOf(a, a.length))
                .toArray(int[][]::new);
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
                if (v == 0) {
                    continue;
                }
                if (i == last && j == last) {
                    h += 1;
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
                if (v == 0) {
                    continue;
                }
                int ei, ej;
                ei = (v - 1) / n;
                ej = (v - 1) % n;
//                System.out.println(String.format("%d is %d exp %d  is %d exp %d", v, i, ei, j, ej));
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
        if (twinBoard != null) {
            return twinBoard;
        }
        
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
        twinBoard = getBoardAfterSwap(r1, c1, r2, c2);
        return twinBoard;
    }

    private Board getBoardAfterSwap(int r1, int c1, int r2, int c2) {
        int[][] tilesCopy = getTilesAfterSwap(r1, c1, r2, c2);
        Board board = new Board(tilesCopy);
        return board;
    }

    private int[][] getTilesAfterSwap(int r1, int c1, int r2, int c2) {
        int[][] tilesCopy = cloneTiles(this.tiles);
        int tmp = tilesCopy[r2][c2];
        tilesCopy[r2][c2] = tilesCopy[r1][c1];
        tilesCopy[r1][c1] = tmp;
        return tilesCopy;
    }
    
    private static void testHammingDistance() {
        System.out.println("Running testHammingDistance...");
        int[][] tiles = {
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        };
        Board board = new Board(tiles);
        int v = board.hamming();
        assert (v == 4) : String.format("expected 4 but got %d", v);
    }
    
    private static void testHammingDistanceWithSize2() {
        System.out.println("Running testHammingDistanceWithSize2...");
        int[][] tiles = {
            {3, 2},
            {1, 0},
        };
        Board board = new Board(tiles);
        int v = board.hamming();
        assert (v == 2) : String.format("expected 2 but got %d", v);
    }
    
    private static void testManhattanDistance() {
        System.out.println("Running testManhattanDistance...");
        int[][] tiles = {
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        };
        Board board = new Board(tiles);
        int v = board.manhattan();
        assert (v == 4) : String.format("expected 4 but got %d", v);
    }
    
    private static void testCallingRandomMethods9Times() {
        System.out.println("Running testCallingRandomMethods9Times...");
        int[][] tiles = {
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        };
        
        int[][] otherTiles = {
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6},
        };
        Board otherBoard = new Board(otherTiles);
        
        int[][] expectedTiles = {
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        };
        Board expectedBoard = new Board(expectedTiles);
        
        Board board = new Board(tiles);
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.toString();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.hamming();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.twin();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.equals(otherBoard);
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.hamming();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.equals(otherBoard);
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.isGoal();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.twin();
        assert board.equals(expectedBoard) : "Expected true but got false";
        
        board.twin();
        assert board.equals(expectedBoard) : "Expected true but got false";
    }
    
    // unit testing (not graded)
    public static void main(String[] args) {
        testHammingDistanceWithSize2();
        testHammingDistance();
        testManhattanDistance();
        testCallingRandomMethods9Times();
    }
}
