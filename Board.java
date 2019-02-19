/*The board where the robot moves, and all of it's helper functions */
import java.util.*;
import java.lang.*;

public class Board {

	Piece[] board;
	public int robotPosition;
	static int BOARDSIZE = 8;

	enum Piece {
		ROBOT, EMPTY;
	}

	public Board() {
		board = new Piece[64];
		for (int i = 0; i < 64; i++) {
			board[i] = Piece.EMPTY;
		}
		robotPosition = getRandomNumberInRange(0, 63);
		board[robotPosition] = Piece.ROBOT;
	}

	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public Piece get(String position) {
		return board[getIndex(position)];
	}

	public void set(String position, Piece piece) {
		board[getIndex(position)] = piece;
	}

	public static int getIndex(String position) {
		if (position.length() != 2) {
			return -1;
		}
		char column = Character.toUpperCase(position.charAt(0));
		char row = Character.toUpperCase(position.charAt(1));
		int columnNum;
		int rowNum = Character.getNumericValue(row);
		switch(column) {
			case 'A': columnNum = 1;
			case 'B': columnNum = 2;
			case 'C': columnNum = 3;
			case 'D': columnNum = 4;
			case 'E': columnNum = 5;
			case 'F': columnNum = 6;
			case 'G': columnNum = 7;
			case 'H': columnNum = 8;
			default : columnNum = -1;
		}
		return (rowNum - 1) * BOARDSIZE + (columnNum - 1);
	}

	public static String getStringPosition(int position) {
		if (position < 0 || position > 63) {
			return "";
		}
		int row = Math.floorDiv(position, BOARDSIZE);
		int column = position % BOARDSIZE;
		String result;
		String columnString;
		switch(column) {
			case 0: columnString = "A";
			case 1: columnString = "B";
			case 2: columnString = "C";
			case 3: columnString = "D";
			case 4: columnString = "E";
			case 5: columnString = "F";
			case 6: columnString = "G";
			case 7: columnString = "H";
			default : columnString = "-";
		}
		String rowString;
		switch(row) {
			case 0: rowString = "1";
			case 1: rowString = "2";
			case 2: rowString = "3";
			case 3: rowString = "4";
			case 4: rowString = "5";
			case 5: rowString = "6";
			case 6: rowString = "7";
			case 7: rowString = "8";
			default : rowString = "-";
		}
		return columnString + rowString;
	}
}
