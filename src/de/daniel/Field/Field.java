package de.daniel.Field;

public class Field {
	protected int[][] field;

	public Field() {
		field = new int[9][9];
	}

	public int[][] getField() {
		return field.clone();
	}

	public boolean setField(int[][] field) {
		this.field = field.clone();
		return true;
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

}
