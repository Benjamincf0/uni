package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		Color[][] grid = board.flatten();
		int dim = grid.length;
		int score = 0;
	
		// Traverse the outside edge
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i == 0 || i == dim - 1) {
					if (grid[i][j].equals(targetGoal)) {
						score++;
						if (j == 0 || j == dim - 1) {
							score++;
						}
					}
				}
				else if (j == 0 || j == dim - 1) {
					if (grid[i][j].equals(targetGoal)) {
						score++;
					}
				}
			}
		}
	
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
