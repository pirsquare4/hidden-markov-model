/*The board where the robot moves, and all of it's helper functions */

public class Board {
	Piece[] Board
	int robotPosition
	enum Piece {
		ROBOT, EMPTY
	}

	public Board() {
		Board = new Piece[64]
		for (int i = 0; i < 64; i++) {
  			System.out.println(i);
		}
	}
}
