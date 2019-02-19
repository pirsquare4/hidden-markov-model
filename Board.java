import java.util.*;
import java.lang.*;

/** 
* Board Simulator Object
* Class for the board structure, simulates the board and moves the robot around the board
* when told to do so.
* @author: Giordano Bonora Groome
*/
public class Board {

	Piece[] board;
	public int robotPosition;
	static int BOARDSIZE = 8;

	enum Piece {
		ROBOT, EMPTY;
	}

	/**
	* Constructs an 8x8 Board, it consists of 63 empty pieces, and 1
	* randomly assigned robot piece.
	*/
	public Board() {
		board = new Piece[64];
		for (int i = 0; i < 64; i++) {
			board[i] = Piece.EMPTY;
		}
		robotPosition = getRandomNumberInRange(0, 63);
		board[robotPosition] = Piece.ROBOT;
	}

	/** 
	* Returns a random number (Taken from StackOverflow) 
	*/
	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	/** 
	* Returns the value of what is at POSITION on the board.
	*/
	public Piece get(String position) {
		return board[getIndex(position)];
	}

	/**
	* Sets POSITION on the board to PIECE.
	*/
	public void set(String position, Piece piece) {
		board[getIndex(position)] = piece;
	}

	/**
	* Moves the robot in the given heading, THROWS an IllegalArgumentException
	* if the heading is invalid, or if the given movement would cause the robot
	* to hit a wall (Exceed the board limitations). 
	* 0 represents movement North, 1 East, 2 South, 3 West. 
	*/ 
	public void move(int heading) {
		if (heading > 3 || heading < 0) {
			throw new IllegalArgumentException("Not a valid heading");
		}
		int movement = 0;
		if (heading == 0) {
			movement = 8;
		} else if (heading == 1) {
			movement = 1;
		} else if (heading == 2) {
			movement = -8;
		} else if (heading == 3) {
			movement = -1;
		}
		if (isAdjacent(robotPosition, robotPosition + movement)) {
			board[robotPosition] = Piece.EMPTY;
			robotPosition += movement;
			board[robotPosition] = Piece.ROBOT;
		} else {
			throw new IllegalArgumentException("This movement would cause the robot to hit a wall");
		}
	}
	/**
	* Converts the string POSITION to its integer equivalent on the board,
	* and returns it.
	*/
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

	/**
	* Converts the integer POSITION to its string equivalent on the board,
	* and returns it.
	*/
	public static String getStringPosition(int position) {
		if (position < 0 || position > 63) {
			return "";
		}
		int row = position/BOARDSIZE;
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

	/**
	* Returns if TRUE if the string POSITION is an edge on the board, else false.
	*/
	public boolean isEdge(String position) {
		return isEdge(getIndex(position));
	}

	/**
	* Returns if TRUE if the integer POSITION is an edge on the board, else false.
	*/
	public boolean isEdge(int position) {
		if (position > 63 || position < 0) {
			return false;
		}
		return position <= 7 || position >= 56 ||
		 position % BOARDSIZE == 0 || position % BOARDSIZE == 7;

	}

	/**
	* Returns if the string positions TILE1 and TILE2 are adjacent on the board.
	*/
	public boolean isAdjacent(String tile1, String tile2) {
		return isAdjacent(getIndex(tile1),getIndex(tile2));
	}

	/**
	* Returns if the integer positions TILE1 and TILE2 are adjacent on the board.
	*/
	public boolean isAdjacent(int tile1, int tile2) {
		if (tile1 == tile2) {
			return false;
		}
		if (tile1 > 63 || tile2 > 63 ) {
			return false;
		} else if  (tile1 < 0 || tile2 < 0) {
			return false;
		} else if ((tile1 % BOARDSIZE == tile2 % BOARDSIZE - 1) ||
		 (tile1 % BOARDSIZE == tile2 % BOARDSIZE) || 
		 (tile1 % BOARDSIZE == tile2 % BOARDSIZE + 1)) {
		 	if (isAbove(tile1, tile2) || isSame(tile1, tile2) || isBelow(tile1, tile2)) {
		 		return true;
		 	}
		 }

		return false;
	}

	/**
	* Returns if the string positions TILE1 and TILE2 are 2 pieces away from
	* each other on the board.
	*/
	public boolean isAdjacent2(String tile1, String tile2) {
		return isAdjacent2(getIndex(tile1),getIndex(tile2));
	}

	/**
	* Returns if the integer positions TILE1 and TILE2 are 2 pieces away from each 
	* other on the board.
	*/
	public boolean isAdjacent2(int tile1, int tile2) {
		if (tile1 == tile2) {
			return false;
		} else if (tile1 > 63 || tile2 > 63 ) {
			return false;
		} else if  (tile1 < 0 || tile2 < 0) {
			return false;
		} else if ((tile1 % BOARDSIZE == tile2 % BOARDSIZE - 2) ||
			(tile1 % BOARDSIZE == tile2 % BOARDSIZE) ||
			(tile1 % BOARDSIZE == tile2 % BOARDSIZE - 2)) {
			if (isAbove2(tile1,tile2) || isSame(tile1, tile2) || isBelow2(tile1,tile2)) {
				return true; /* is Two above, two below, or on the same line, and two
								to the left, two to the right or the the same row.
								Note: Dealt with edge case tile1 = tile2 */
			}
		}
		return false;
	}

	/** 
	* Returns true if tile1 is in the row above tile2.
	*/
	private boolean isAbove(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == -1;
	}

	/**
	* Returns true if tile1 is on the same row as tile2.
	*/
	private boolean isSame(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 0;
	}

	/** 
	* Returns true if tile1 is in the row below tile2.
	*/
	private boolean isBelow(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 1;
	}

	/** 
	* Returns true if tile1 is 2 rows above tile2.
	*/
	private boolean isAbove2(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == -2;
	}

	/** 
	* Returns true if tile1 is 2 row below tile2.
	*/
	private boolean isBelow2(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 2;
	}
}
