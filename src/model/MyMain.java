package model;
import java.util.*;

public class MyMain {
	public static void main(String[] args) {
		double sum = 0;
		double[] EmptyUpdate = new double[64];
		for (int i = 0; i < 64; i++) {
			double newVal = Board.GioTest(i);
			sum += newVal;
			EmptyUpdate[i] = newVal;
		}
		EmptyUpdate = Board.normalize(EmptyUpdate, sum);
		System.out.println(Arrays.toString(EmptyUpdate));
	}
}