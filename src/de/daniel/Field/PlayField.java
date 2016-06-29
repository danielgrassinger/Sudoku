package de.daniel.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PlayField extends Field {

	public void init() {
		
//		ArrayList<Integer> list = new ArrayList<Integer>(10);
//		for (int i = 1; i < 10; i++) {
//			list.add(i);
//		}
//		for (int i = 0; i < 9; i++) {
//			Collections.shuffle(list);
//			do {
//				for (int j = 0; j < 9; j++) {
//					
//					field[i][j] = list.get(j);
//				}
//			} while (!isCorrect());
//		}
		
		final int n = 3;
		for (int i = 0; i < n*n; i++){
			for (int j = 0; j < n*n; j++){
				field[i][j] = (i*n + i/n + j) % (n*n) + 1;
			}
		}
		Random rand = new Random();
		for(int i = 0; i<100;i++){
			
		}
		
	}

}
