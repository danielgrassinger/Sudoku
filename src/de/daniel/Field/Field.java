package de.daniel.Field;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Field {
	protected int[][] field;
	protected int[][] initField;

	public Field() {
		field = new int[9][9];
		initField= new int[9][9];
	}

	public int[][] getField() {
		return field.clone();
	}
	
	public int[][] getInitField(){
		return initField.clone();
	}

	public boolean setField(int[][] field) {
		this.field = field.clone();
		return true;
	}
	
	public void setInitField(int[][] field){
		initField=field.clone();
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

		Deque<Integer> stack;

		boolean ex = false;
		do {
			newField = new int[9][9];

			Random random = new Random();

			stack = new ArrayDeque<Integer>(20);
			for (int i = 0; i < 20; i++) {
				stack.add((i % 9) + 1);
			}

			for (int i = 0; i < 20; i++) {
				boolean terminate = false;
				do {
					int x = Math.abs(random.nextInt()) % 9;
					int y = Math.abs(random.nextInt()) % 9;
					if (newField[y][x] == 0) {
						if (isSafe(newField, y, x, (int) stack.peek())) {
							newField[y][x] = stack.poll();
							terminate = true;
						}
					}
				} while (!terminate);
			}
			FieldSolver solver = new FieldSolver();
			int[][] testField = new int[9][9];
			for(int i = 0;i<9;i++){
				for(int j=0;j<9;j++){
					testField[i][j]=newField[i][j];
				}
			}
			solver.setField(testField);
			if (solver.solve()) {
				ex = true;
			}
		} while (!ex);

		field = newField;
		setInitField(newField);
	}

	private boolean isSafe(int grid[][], int row, int col, int num) {
		/*
		 * Check if 'num' is not already placed in current row, current column
		 * and current 3x3 box
		 */
		return checkRow(grid, row, num) && checkColumn(grid, col, num) && checkBox(grid, row, col, num);
	}
	
	public boolean isCorrect() {
		
		return (checkColumns()&& checkRows() && checkBoxes());
	}

	private boolean checkColumns() {
		for (int i = 0; i < 9; i++) {
			if (!checkColumn(i))
				return false;
		}
		return true;
	}

	protected boolean checkColumn(int column) {
		int[] checkField = new int[9];
		for (int i = 0; i < 9; i++) {
			checkField[i] = field[i][column];
		}

		return checkArray(checkField);

	}

	private boolean checkRows() {
		for (int i = 0; i < 9; i++) {
			if (!checkRow(i))
				return false;
		}
		return true;
	}

	protected boolean checkRow(int row) {
		int[] checkField = new int[9];
		for (int i = 0; i < 9; i++) {
			checkField[i] = field[row][i];
		}

		return checkArray(checkField);
	}

	private boolean checkBoxes() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				if (!checkBox(j,i))
					return false;
			}
		}
		return true;
	}

	protected boolean checkBox(int x, int y) {
		int[] checkField = new int[9];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				checkField[3 * j + i] = field[i][j];
			}
		}

		return checkArray(checkField);
	}

	private boolean checkArray(int[] array) {

		for (int i = 0; i < 9 - 1; i++) {
			if (array[i] <= 9 && array[i] >= 1) {
				for (int j = i + 1; j < 9; j++) {
					if (array[i] == array[j])
						return false;
				}
			}
		}
		return true;
	}
	
	public boolean solve() {

		int[][] solveField = field.clone();
		if (solveSudoku(0, 0)) {
			field = solveField;
			return true;
		} else {
			//System.err.println("was not possible to solve");
			return false;
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
		for (int y = 0; y < 3; y++){
			for (int x = 0; x < 3; x++){
				if (grid[rowstart*3 +y][colstart*3+x] == num){
					return false;
				}
			}
		}
		return true;
	}

}
