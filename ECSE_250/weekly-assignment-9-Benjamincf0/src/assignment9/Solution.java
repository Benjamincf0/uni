package assignment9;

public class Solution {
    public int getNumPaths(int[][] grid) {
        if (grid[0][0] == 1 || grid[grid.length - 1][grid[0].length - 1] == 1) {
            return 0;
        }
        return numNextPaths(0, 0, grid);
    }

    public int numNextPaths(int i, int j, int[][] grid) {
        if (i >= grid.length || j >= grid[0].length || grid[i][j] == 1) {
            return 0;
        }

        if (i == grid.length - 1 && j == grid[0].length - 1) {
            return 1;
        }

        int rightPaths = numNextPaths(i, j + 1, grid);
        int downPaths = numNextPaths(i + 1, j, grid);

        return rightPaths + downPaths;
    }
}
