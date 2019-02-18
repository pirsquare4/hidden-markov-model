/*The board where the robot moves, and all of it's helper functions */

public class Board {
	Piece[] board
	int robotPosition
	enum Piece {
		ROBOT, EMPTY
	}

	public Board() {
		board = new Piece[64]
		for (int i = 0; i < 64; i++) {
			board = EMPTY
		}
	}
}
