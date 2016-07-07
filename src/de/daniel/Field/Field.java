package de.daniel.Field;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * This class is the play field and has metods to solve the Sudoku.
 *
 */
public class Field {
	private final int fieldsize=9;

	// the actual play field
	protected int[][] field;

	// the play field when game was started or loaded
	// this field is used to reset the field
	protected int[][] initField;

	public Field() {
		field = new int[fieldsize][fieldsize];
		initField = new int[fieldsize][fieldsize];
	}

	/**
	 * This method returns the actual play field.
	 * 
	 * @return the actual play field
	 */
	public int[][] getField() {
		return field.clone();
	}

	/**
	 * This method returns the play field on its initial state.
	 * 
	 * @return the play field on its initial state
	 */
	public int[][] getInitField() {
		return initField.clone();
	}

	/**
	 * This method set the current play field.
	 * 
	 * @param field
	 * @return True if the field could be set, false if the field could not be
	 *         set
	 */
	public boolean setField(int[][] field) {
		this.field = field.clone();
		return true;
	}

	/**
	 * This method set the initial play field.
	 * 
	 * @param field
	 */
	public void setInitField(int[][] field) {
		initField = field.clone();
		setField(field);
	}

	public void init() {

		// final int n = 3;
		// for (int i = 0; i < n*n; i++){
		// for (int j = 0; j < n*n; j++){
		// field[i][j] = (i*n + i/n + j) % (n*n) + 1;
		// }
		// }
		// Random rand = new Random();
		// for(int i = 0; i<100;i++){
		//
		// }

		int[][] newField;

		// stack to set twenty initial numbers in the play field
		Deque<Integer> stack;

		boolean exit = false;

		do {
			// init a new play field array
			newField = new int[fieldsize][fieldsize];

			// generate new Random
			Random random = new Random();

			// fill the stack with twenty numbers from 1 to 9
			stack = new ArrayDeque<Integer>(20);
			for (int i = 0; i < 25; i++) {
				stack.add((i % fieldsize) + 1);
			}

			// place the twenty numbers in the play field
			for (int i = 0; i < 25; i++) {
				boolean terminate = false;
				do {
					// find a x and y position for number
					int x = Math.abs(random.nextInt()) % fieldsize;
					int y = Math.abs(random.nextInt()) % fieldsize;

					// if the field is empty and the number fit in than set the
					// number
					if (newField[y][x] == 0) {
						if (isSafe(newField, y, x, (int) stack.peek())) {
							newField[y][x] = stack.poll();
							terminate = true;
						}
					}
				} while (!terminate);
			}

			// check if it is possible to solve this Sudoku
			int[][] testField = new int[fieldsize][fieldsize];

			// copy the field
			for (int i = 0; i < fieldsize; i++) {
				for (int j = 0; j < fieldsize; j++) {
					testField[i][j] = newField[i][j];
				}
			}

			// try to solve the Sudoku
			field = testField;
			if (solve()) {
				exit = true;
			}
		} while (!exit);

		// set the play field
		field = newField;
		setInitField(newField);
	}

	/**
	 * This method check if the number would fit in this field.
	 * 
	 * @param grid
	 *            the current Sudoku play field
	 * @param row
	 *            the row of the field
	 * @param col
	 *            the column of the field
	 * @param num
	 *            the number which is tested to fit in this field
	 * @return returns if the number fit in the Sudoku grid
	 */
	private boolean isSafe(int grid[][], int row, int col, int num) {
		/*
		 * Check if 'num' is not already placed in current row, current column
		 * and current 3x3 box
		 */
		return checkRow(grid, row, num) && checkColumn(grid, col, num) && checkBox(grid, row, col, num);
	}

	/**
	 * This method solve the Sudoku.
	 * 
	 * @return if it was possible to solve this Sudoku
	 */

	public boolean solve() {

		if (solveSudoku(0, 0)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method is a helper function to solve the Sudoku.
	 * 
	 * @param row
	 *            row of the next field
	 * @param col
	 *            column of the next field
	 *
	 */
	private boolean solveSudoku(int row, int col) {

		// If there is no unassigned location, we are done
		if (!FindUnassignedLocation(field)) {
			return true; // success!
		}

		while (field[row][col] != 0) {
			row = (col == fieldsize-1 ? row + 1 : row);
			col = (col + 1) % fieldsize;

		}
		// consider digits 1 to 9
		for (int num = 1; num <= fieldsize; num++) {

			if (isSafe(field, row, col, num)) {

				field[row][col] = num;

				// return, if success
				if (solveSudoku((col == fieldsize-1 ? row + 1 : row), (col + 1) % fieldsize))
					return true;

				// failure, unmake & try again
				field[row][col] = 0;
			}
		}
		return false; // this triggers backtracking
	}

	/**
	 * This method check if the Sudoku is completely solved.
	 * 
	 * @param grid
	 *            The play field to be checked.
	 * @return if the Sudoku is completely solved.
	 */
	private boolean FindUnassignedLocation(int grid[][]) {

		for (int row = 0; row < fieldsize; row++)
			for (int col = 0; col < fieldsize; col++)
				if (grid[row][col] == 0)
					return true;
		return false;
	}

	/**
	 * This is method if the number would fit in this row.
	 * 
	 * @param grid
	 *            the current Sudoku play field
	 * @param row
	 *            the row of the field
	 * @param num
	 *            the number which is tested to fit in this field
	 * @return returns if the number fit in the Sudoku grid
	 */
	private boolean checkRow(int grid[][], int row, int num) {
		for (int i = 0; i < fieldsize; i++) {
			if (grid[row][i] == num)
				return false;
		}
		return true;
	}

	/**
	 * This is method if the number would fit in this row.
	 * 
	 * @param grid
	 *            the current Sudoku play field
	 * @param num
	 *            the number which is tested to fit in this field
	 * @return returns if the number fit in the Sudoku grid
	 */
	private boolean checkColumn(int grid[][], int column, int num) {
		for (int i = 0; i < fieldsize; i++) {
			if (grid[i][column] == num)
				return false;
		}
		return true;

	}

	/**
	 * This is method if the number would fit in this row.
	 * 
	 * @param grid
	 *            the current Sudoku play field
	 * @param row
	 *            the row of the field
	 * @param num
	 *            the number which is tested to fit in this field
	 * @return returns if the number fit in the Sudoku grid
	 */
	private boolean checkBox(int grid[][], int row, int col, int num) {
		final int boxsize=(int)Math.sqrt(fieldsize);
		int rowstart = row / boxsize;
		int colstart = col / boxsize;
		for (int y = 0; y < boxsize; y++) {
			for (int x = 0; x < boxsize; x++) {
				if (grid[rowstart * boxsize + y][colstart * boxsize + x] == num) {
					return false;
				}
			}
		}
		return true;
	}

}
