package de.daniel.GUI;

import de.daniel.Field.Field;

public class Sudoku {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Field field = new Field();
		int[][] myfield = {{-1,2,3,3,5,6,-1,8,9},{2},{3},{4},{5},{6},{7},{8},{9}};
		
		field.setField(myfield);
		System.out.println(field.isCorrect());
	}

}
