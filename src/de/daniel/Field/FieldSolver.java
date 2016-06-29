package de.daniel.Field;

public class FieldSolver extends Field {
	// int row = 0;
	// int col = 0;

	public void solveSudokuExtern() {

		int[][] solveField = field.clone();
		if (solveSudoku(0, 0)) {
			field = solveField;
		} else {
			System.err.println("Sudoku was no solved");
		}

	}

	private boolean solveSudoku(int row, int col) {

		// If there is no unassigned location, we are done
		if (!FindUnassignedLocation(field, row, col))
			return true; // success!

		while (field[row][col] != 0) {
			row = (col == 8 ? row + 1 : row);
			col = (col + 1) % 9;

		}
		// consider digits 1 to 9
		for (int num = 1; num <= 9; num++) {
			// if looks promising
			if (isSafe(field, row, col, num)) {
				// make tentative assignment
				field[row][col] = num;

				// return, if success, yay!
				if (solveSudoku((col == 8 ? row + 1 : row), (col + 1) % 9))
					return true;

				// failure, unmake & try again
				field[row][col] = 0;
			}
		}
		return false; // this triggers backtracking
	}

	private boolean FindUnassignedLocation(int grid[][], int row, int col) {
		for (row = 0; row < 9; row++)
			for (col = 0; col < 9; col++)
				if (grid[row][col] == 0)
					return true;
		return false;
	}

	private boolean isSafe(int grid[][], int row, int col, int num) {
		/*
		 * Check if 'num' is not already placed in current row, current column
		 * and current 3x3 box
		 */
		return checkRow(grid, row, num) && checkColumn(grid, col, num) && checkBox(grid, row, col, num);
	}

	private boolean checkRow(int grid[][], int row, int num) {
		for (int i = 0; i < 9; i++) {
			if (grid[row][i] == num)
				return false;
		}
		return true;
	}

	private boolean checkColumn(int grid[][], int column, int num) {
		for (int i = 0; i < 9; i++) {
			if (grid[i][column] == num)
				return false;
		}
		return true;

	}

	private boolean checkBox(int grid[][], int row, int col, int num) {
		int rowstart=row/3;
		int colstart=col/3;
		for (int y = 0; row < 3; row++)
			for (int x = 0; col < 3; col++)
				if (grid[rowstart +y][colstart+x] == num)
					return false;
		return true;
	}
}
