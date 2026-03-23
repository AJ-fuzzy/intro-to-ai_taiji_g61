import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Board {
    public enum Cell { EMPTY, BLACK, WHITE }

    private final int size;
    private final Cell[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        for (Cell[] row : grid)
            Arrays.fill(row, Cell.EMPTY);
    }

    /** Copy constructor — deep-copies the grid state. */
    public Board(Board other) {
        this.size = other.size;
        this.grid = new Cell[size][size];
        for (int r = 0; r < size; r++)
            this.grid[r] = Arrays.copyOf(other.grid[r], size);
    }

    public int getSize() { return size; }

    public void copyFrom(Board other) {
        for (int r = 0; r < size; r++){
            this.grid[r] = Arrays.copyOf(other.grid[r], size);
        }
    }

    public Cell get(int row, int col) { return grid[row][col]; }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /** Place a domino. Returns false if placement is invalid. */
    public boolean placeDomino(int r1, int c1, Cell color1, int r2, int c2, Cell color2) {
        if (!inBounds(r1, c1) || !inBounds(r2, c2)) return false;
        if (grid[r1][c1] != Cell.EMPTY || grid[r2][c2] != Cell.EMPTY) return false;
        // Must be adjacent
        if (Math.abs(r1 - r2) + Math.abs(c1 - c2) != 1) return false;
        if (color1 == Cell.EMPTY || color2 == Cell.EMPTY) return false;

        grid[r1][c1] = color1;
        grid[r2][c2] = color2;
        return true;
    }

    public boolean isFull() {
        for (Cell[] row : grid)
            for (Cell c : row)
                if (c == Cell.EMPTY) return false;
        return true;
    }

    public boolean hasNoMoves() {
        int[] dr = {0, 1};
        int[] dc = {1, 0};
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c] != Cell.EMPTY) continue;
                for (int d = 0; d < 2; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if (inBounds(nr, nc) && grid[nr][nc] == Cell.EMPTY) return false;
                }
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return isFull() || hasNoMoves();
    }

    /** Returns the size of the largest connected group for the given color. */
    public int largestGroup(Cell color) {
        boolean[][] visited = new boolean[size][size];
        int max = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!visited[r][c] && grid[r][c] == color) {
                    int groupSize = bfs(r, c, color, visited);
                    max = Math.max(max, groupSize);
                }
            }
        }
        return max;
    }

    private int bfs(int startR, int startC, Cell color, boolean[][] visited) {
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC});
        visited[startR][startC] = true;
        int count = 0;
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            count++;
            for (int d = 0; d < 4; d++) {
                int nr = cur[0] + dr[d];
                int nc = cur[1] + dc[d];
                if (inBounds(nr, nc) && !visited[nr][nc] && grid[nr][nc] == color) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
        return count;
    }

    /** Count total cells of a given color. */
    public int countCells(Cell color) {
        int count = 0;
        for (Cell[] row : grid)
            for (Cell c : row)
                if (c == color) count++;
        return count;
    }

    public void print() {
        System.out.print("   ");
        for (int c = 0; c < size; c++)
            System.out.printf("%2d ", c);
        System.out.println();
        System.out.print("   ");
        System.out.println("---".repeat(size));
        for (int r = 0; r < size; r++) {
            System.out.printf("%2d|", r);
            for (int c = 0; c < size; c++) {
                String s = switch (grid[r][c]) {
                    case BLACK -> " B ";
                    case WHITE -> " W ";
                    case EMPTY -> " . ";
                };
                System.out.print(s);
            }
            System.out.println("|");
        }
        System.out.print("   ");
        System.out.println("---".repeat(size));
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                sb.append(grid[r][c].ordinal());
            }
        }
        return sb.toString();
    }
}
