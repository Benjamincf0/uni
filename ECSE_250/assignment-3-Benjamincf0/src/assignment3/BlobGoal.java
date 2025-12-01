package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		Color[][] grid = board.flatten();
        int n = grid.length;
        boolean[][] seen = new boolean[n][n];
        int maxBlob = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (!seen[row][col] && grid[row][col].equals(targetGoal)) {
                    int blobSize = undiscoveredBlobSize(row, col, grid, seen);
                    maxBlob = Math.max(maxBlob, blobSize);
                }
            }
        }

        return maxBlob;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		/*
		 * ADD YOUR CODE HERE
		 */
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[0].length || visited[i][j] || !unitCells[i][j].equals(targetGoal)) {
			return 0;
		}
	
		visited[i][j] = true;
		int size = 1;
	
		size += undiscoveredBlobSize(i - 1, j, unitCells, visited); // Up
		size += undiscoveredBlobSize(i + 1, j, unitCells, visited); // Down
		size += undiscoveredBlobSize(i, j - 1, unitCells, visited); // Left
		size += undiscoveredBlobSize(i, j + 1, unitCells, visited); // Right
	
		return size;
	}

}
