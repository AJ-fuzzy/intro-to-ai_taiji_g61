package com.taiji;

import java.util.*;

public class TaijiBoard {
    private final int size;
    private final int[][] grid; // 0: Empty, 1: Black, 2: White

    public TaijiBoard(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public boolean isValidMove(int r1, int c1, int r2, int c2) {
        return r1 >= 0 && r1 < size && c1 >= 0 && c1 < size &&
                r2 >= 0 && r2 < size && c2 >= 0 && c2 < size &&
                grid[r1][c1] == 0 && grid[r2][c2] == 0 &&
                (Math.abs(r1 - r2) + Math.abs(c1 - c2) == 1);
    }

    public void placeTile(int r1, int c1, int r2, int c2, int color1) {
        grid[r1][c1] = color1;
        grid[r2][c2] = (color1 == 1) ? 2 : 1;
    }

    public void removeTile(int r1, int c1, int r2, int c2) {
        grid[r1][c1] = 0;
        grid[r2][c2] = 0;
    }

    public List<Integer> getClusterSizes(int color) {
        boolean[][] visited = new boolean[size][size];
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == color && !visited[i][j]) {
                    sizes.add(dfs(i, j, color, visited));
                }
            }
        }
        sizes.sort(Collections.reverseOrder());
        return sizes.isEmpty() ? List.of(0) : sizes;
    }

    private int dfs(int r, int c, int color, boolean[][] visited) {
        if (r < 0 || r >= size || c < 0 || c >= size || grid[r][c] != color || visited[r][c]) return 0;
        visited[r][c] = true;
        return 1 + dfs(r + 1, c, color, visited) + dfs(r - 1, c, color, visited) +
                dfs(r, c + 1, color, visited) + dfs(r, c - 1, color, visited);
    }

    public int getSize() { return size; }
    public int[][] getGrid() { return grid; }
}