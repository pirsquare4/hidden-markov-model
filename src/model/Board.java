package model;

import java.util.*;
import java.lang.*;

/** 
* Board Simulator Object
* Class for the board structure, simulates the board and moves the robot around the board
* when told to do so.
* @author: Giordano Bonora Groome & Ben Chu
*/
public class Board {

	Piece[] board;
	public int position;
	public static int BOARDSIZE = 8;

	/* Piece types */
	enum Piece {
		ROBOT, EMPTY;
	}
	public static void main(String[] args) {
	 	Board board = new Board(3);
	 	board.PrintBoard();
	}

	/**
	* Constructs an 8x8 Board, it consists of 63 empty pieces, and 1
	* randomly assigned robot piece.
	*/
	public Board(int pos) {
		board = new Piece[64];
		for (int i = 0; i < 64; i++) {
			board[i] = Piece.EMPTY;
		}
		position = pos;
		board[position] = Piece.ROBOT;
	}

	/** 
	* Returns a random number min<=x<=max (Taken from StackOverflow) 
	*/
	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
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
			return "null";
		}
		int row = position/BOARDSIZE;
		int column = position % BOARDSIZE;
		String result;
		String columnString;
		switch(column) {
			case 0: 
			columnString = "A";
			break;

			case 1: 
			columnString = "B";
			break;

			case 2: 
			columnString = "C";
			break;

			case 3: 
			columnString = "D";
			break;
			case 4: 
			columnString = "E";
			break;
			case 5: 
			columnString = "F";
			break;
			case 6: 
			columnString = "G";
			break;
			case 7: 
			columnString = "H";
			break;
			default : columnString = "-";
		}
		String rowString;
		switch(row) {
			case 0: 
			rowString = "1";
			break;

			case 1: 
			rowString = "2";
			break;

			case 2: 
			rowString = "3";
			break;

			case 3: 
			rowString = "4";
			break;

			case 4: 
			rowString = "5";
			break;

			case 5: 
			rowString = "6";
			break;

			case 6: 
			rowString = "7";
			break;

			case 7: 
			rowString = "8";
			break;

			default : 
			rowString = "-";
			break;
		}
		return columnString + rowString;
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
			movement = BOARDSIZE;
		} else if (heading == 1) {
			movement = 1;
		} else if (heading == 2) {
			movement = BOARDSIZE * -1;
		} else if (heading == 3) {
			movement = -1;
		}
		if (isAdjacent(position, position + movement)) {
			board[position] = Piece.EMPTY;
			position += movement;
			board[position] = Piece.ROBOT;
		} else {
			throw new IllegalArgumentException("This movement would cause the robot to hit a wall");
		}
	}

	/**
	* Returns if TRUE if the string POSITION is an edge on the board, else false.
	*/
	public static boolean isEdge(String position) {
		return isEdge(getIndex(position));
	}

	/**
	* Returns if TRUE if the integer POSITION is an edge on the board, else false.
	*/
	public static boolean isEdge(int position) {
		if (position > BOARDSIZE * BOARDSIZE - 1 || position < 0) {
			return false; //out of bounds
		}	
					/*A1 - A8*/		 			/*H1 - H8*/
		return position <= BOARDSIZE - 1 || position >= BOARDSIZE * (BOARDSIZE - 1) ||
		 position % BOARDSIZE == 0 || position % BOARDSIZE == 7;
	}		/*A1 - H1*/						/*A8 - H8*/

	/**
	* Returns if the string positions TILE1 and TILE2 are adjacent on the board.
	*/
	public static boolean isAdjacent(String tile1, String tile2) {
		return isAdjacent(getIndex(tile1),getIndex(tile2));
	}

	/**
	* Returns if the integer positions TILE1 and TILE2 are adjacent on the board.
	*/
	public static boolean isAdjacent(int tile1, int tile2) {
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

	public static boolean isNWSE(int tile1, int tile2) {
		if (tile1 == tile2) {
			return false;
		} else if (tile1 > 63 || tile2 > 63 ) {
			return false;
		} else if  (tile1 < 0 || tile2 < 0) {
			return false;
		} else {
			return (tile1 % BOARDSIZE == tile2 % BOARDSIZE - 1 && isSame(tile1, tile2)) ||
					(tile1 % BOARDSIZE == tile2 % BOARDSIZE + 1 && isSame(tile1, tile2)) ||
					(tile1 % BOARDSIZE == tile2 && isBelow(tile1, tile2))||
					(tile1 % BOARDSIZE == tile2 && isAbove(tile1, tile2));
		}

	}

	/**
	* Returns if the string positions TILE1 and TILE2 are 2 pieces away from
	* each other on the board.
	*/
	public static boolean isAdjacent2(String tile1, String tile2) {
		return isAdjacent2(getIndex(tile1),getIndex(tile2));
	}

	/**
	* Returns if the integer positions TILE1 and TILE2 are 2 pieces away from each 
	* other on the board.
	*/
	public static boolean isAdjacent2(int tile1, int tile2) {
		if (tile1 == tile2) {
			return false;
		} else if (tile1 > 63 || tile2 > 63 ) {
			return false;
		} else if  (tile1 < 0 || tile2 < 0) {
			return false;
		} else if ((tile1 % BOARDSIZE == tile2 % BOARDSIZE + 2) ||
			(tile1 % BOARDSIZE == tile2 % BOARDSIZE) ||
			(tile1 % BOARDSIZE == tile2 % BOARDSIZE - 2)) {
			if (isAbove2(tile1,tile2) || isSame(tile1, tile2) || isBelow2(tile1,tile2)) {
				return true;
			}
				/*top left, top middle, top right, middle left, middle right, bottom left, bottom right, bottom middle
				 * 8/16 cases */
		} else if ((tile1 % BOARDSIZE == tile2 % BOARDSIZE - 1) ||
				(tile1 % BOARDSIZE == tile2 % BOARDSIZE + 1)) {
			return (isAbove2(tile1,tile2) || isBelow2(tile1,tile2));
				/*top middle-left, top middle-right, bottom middle-left, bottom middle-right
				 * 4/16 cases */
		}

		if ((tile1 % BOARDSIZE == tile2 % BOARDSIZE + 2) || (tile1 % BOARDSIZE == tile2 % BOARDSIZE - 2)) {
			return isAbove(tile1, tile2) || isBelow(tile1, tile2);
				/*other 4 cases */
		}
		return false;
	}

	/** 
	* Returns true if tile1 is in the row above tile2.
	*/
	private static boolean isAbove(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == -1;
	}

	/**
	* Returns true if tile1 is on the same row as tile2.
	*/
	private static boolean isSame(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 0;
	}

	/** 
	* Returns true if tile1 is in the row below tile2.
	*/
	private static boolean isBelow(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 1;
	}

	/** 
	* Returns true if tile1 is 2 rows above tile2.
	*/
	private static boolean isAbove2(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == -2;
	}

	/** 
	* Returns true if tile1 is 2 row below tile2.
	*/
	private static boolean isBelow2(int tile1, int tile2) {
		return tile1/BOARDSIZE - tile2/BOARDSIZE == 2;
	}

	/**
	* Prints out a visual representation of the board.
	*/
	public void PrintBoard() {
		for (int i = 56; i >= 0; i = i - 8) {
			switch (i) {
				case 56:
				System.out.print("8 ");
				break;

				case 48: 
				System.out.print("7 ");
				break;

				case 40: 
				System.out.print("6 ");
				break;

		    	case 32: 
		    	System.out.print("5 ");
		    	break;

		    	case 24: 
		    	System.out.print("4 ");
		    	break;

		    	case 16: 
		    	System.out.print("3 ");
		    	break;

		    	case 8: 
		    	System.out.print("2 ");
		    	break;

		    	case 0: 
		    	System.out.print("1 ");
		    	break;
	    	}
			for (int j = 0; j < 8; j++) {
				Piece color = board[i + j];
				switch (color) {
				case ROBOT:
					System.out.print("R");
					break;
				case EMPTY:
					System.out.print("-");
					break;
				}
				System.out.print(" ");
			}
			System.out.println("");
		}
		System.out.println("  A B C D E F G H");
	}

	/** 
	* Returns the ROBOTPOSITION on the board.
	*/
	public int getRobotPosition() {
		return position;
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

	public static boolean willHitWall(int heading, int tile) {
		if (tile % 8 == 0 && heading == 3) {
			return true;
		} else if (tile / 8 == 0 && heading == 2) {
			return true;
		} else if (tile % 8 == 7 && heading == 1) {
			return true;
		} else if (tile / 8 == 7 && heading == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @param tile is a tile on the board (0<=tile<=63)
	 * @return the valid directions that the robot could go
	 * given a spot on the board.
	 */
	public static int[] validHeadings(int tile) {
		ArrayList<Integer> valid = new ArrayList<Integer>();
		if (isAdjacent(tile, tile + 8)) {
			valid.add(0);
		}
		if (isAdjacent(tile, tile + 1)) {
			valid.add(1);
		}
		if (isAdjacent(tile, tile - 8)) {
			valid.add(2);
		}
		if (isAdjacent(tile, tile - 1)) {
			valid.add(3);
		}
		int[] arr = new int[valid.size()];

		for(int i = 0; i < valid.size(); i++) {
		    if (valid.get(i) != null) {
		        arr[i] = valid.get(i);
		    }
		}
		return arr;
	}

	/**
	 * Returns true if ARR contains the value TARGETVALUE
	 */
	public static boolean contains(int[] arr, int targetValue) {
	    for (int s: arr) {
	        if (s == targetValue) {
	            return true;
	        }
	    }
	    return false;
	}

	/**
	 *
	 * @return BOARDSIZE
	 */
    public int getBoardSize() {
    	return BOARDSIZE;
    }

	/**
	 * Returns the location of MYROBOT in an X,Y pair. X being the row
	 * Y being the column
	 */
	public int[] getXY(robot myRobot) {
		int[] result = new int[3];
		int currentPosition = this.getRobotPosition();
		int Y = position % 8;
		int X = position / 8;
		int H = myRobot.heading;
		result[0] = X;
		result[1] = Y;
		result[2] = H;
		return result;
	}

	public static int XYtoInt(int x, int y) {
		return arr[0] * 8 + arr[1];
	}

	/**
	 * Translates the direction (heading) from the Board heading to the
	 * given Interface GUI heading. West and East remain the same, but North => South
	 * and South => North in from our model to the given GUI model.
	 */
	public int translateDirection(int direction) {
		if (direction == 0) {
			return 2;
		} else if (direction == 2) {
			return 0;
		} else {
			return direction;
		}
	}

	public static int[] removeCurrentDirection(int[] headings, int heading) {
		if (contains(headings, heading)) {
			int[] newHeadings = new int[headings.length - 1];
			int i = 0;
			for (int head: headings) {
				if (head != heading) {
					newHeadings[i] = head;
					i++;
				}
			}
			return newHeadings;
		} else {
			return headings;
		}
	}

	public static double GioTest(int position) {
		int nLn1 = 0;
		int nLn2 = 0;
		int size = 8;
		int[] possibleLn2s = {
				size*2 - 2, size*2 - 1, size *2, size*2 + 1, size * 2 + 2,
				size - 2, size + 2,
				-2, 2,
				size*-1 - 2, size*-1 + 2,
				size*-2 - 2, size*-2 - 1, size*-2, size*-2 + 1, size*-2 + 2};
		int[] possibleLn1s = {size - 1, size, size + 1,
				-1, 1,
				size*-1 - 1, size*-1, size*-1 + +1 };
		for (int possibleLn2: possibleLn2s) {
			if (Board.isAdjacent2(position, position + possibleLn2)) {
				nLn2 += 1;
			}
		}
		for (int possibleLn1: possibleLn1s) {
			if (Board.isAdjacent(position, position + possibleLn1)) {
				nLn1 += 1;
			}
		}
		double chanceOfNothing = 1000 - 100 - nLn1* 50 - nLn2*25;
		return chanceOfNothing;
	}

	public static double[] normalize(double[] arr, double normalizer) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i] / normalizer;
		}
		return arr;
	}

	public static double[][] multiplicar(double[][] A, double[][] B) {

		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
		}

		double[][] C = new double[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				C[i][j] = 0.00000;
			}
		}

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
}	}

