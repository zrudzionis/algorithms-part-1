import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Random;

public class Board implements Iterable<Board> {
    private int[][] tiles;
    private int n;
    private Integer hammingDistance = null;
    private Integer manhattanDistance = null;
    private Integer hashCode = null;

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
        String[] rows = new String[n];
        for (int i = 0; i < n; i++) {
            rows[i] = rowToString(tiles[i]);
        }
        String board = String.join("\n", rows);
        return board;
    }

    private String rowToString(int[] row) {
        String s = "";
        for(int i = 0; i < n; i++) {
            s += Integer.toString(row[i]);
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingDistance != null) {
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
        return h;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance != null) {
            return manhattanDistance;
        }

        int m = 0;
        int last = n - 1;
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
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object y) {
        return this.hashCode() == y.hashCode();
    }

    @Override
    public int hashCode() {
        if (hashCode != null) {
            return hashCode;
        }
        return Arrays.deepHashCode(tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return this;
    }

    public Iterator<Board> iterator() {
        return new BoardIterator();
    }

    private class BoardIterator implements Iterator<Board> {
        int zr, zc;
        int nr, nc;
        int rows[] = new int[] {0, 0, 1, -1};
        int cols[] = new int[] {1, -1, 0, 0};
        int idx = 0;

        public BoardIterator() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (tiles[i][j] == 0) {
                        zr = i;
                        zc = j;
                        break;
                    }
                }
            }
        }

        public boolean hasNext() {
            while (idx < 4) {
                nr = zr + rows[idx];
                nc = zc + cols[idx];
                if (nr >= n || nr < 0 || nc >= n || nc < 0) {
                    idx += 1;
                }
            }
            return idx < 4;
        }

        public Board next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Iterator does not have next");
            } else {
                return getBoardAfterSwap(zr, zc, nr, nc);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    // a board that is o/btained by exchanging any pair of tiles
    public Board twin() {
        Random randomGenerator = new Random();
        int r1 = randomGenerator.nextInt(n);
        int c1 = randomGenerator.nextInt(n);
        int r2 = randomGenerator.nextInt(n);
        int c2 = randomGenerator.nextInt(n);
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

    }
}
