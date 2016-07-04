package de.daniel.Field;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PlayField extends Field {

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
		int rowstart = row / 3;
		int colstart = col / 3;
		for (int y = 0; row < 3; row++)
			for (int x = 0; col < 3; col++)
				if (grid[rowstart + y][colstart + x] == num)
					return false;
		return true;
	}

}
