package model;
import java.util.*;

public class MyMain {
	public static void main(String[] args) {
		boolean yes = true;
		double sum = 0;
		double[] allVals = new double[64];
	 	while (yes) {
	 		for (int i = 0; i < 64; i++) {
				System.out.print("Percentage for position " + Board.getStringPosition(i)+ " is:");
				double newVal = Board.GioTest(i);
				sum += newVal;
				System.out.println(newVal);
				allVals[i] = newVal;
			}
	 		yes = false;
	 	}
	}
}