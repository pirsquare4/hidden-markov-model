/*The board where the robot moves, and all of it's helper functions */
import java.util
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
		robotPosition = getRandomNumberInRange(0, 63)
		board[randomPosition] = ROBOT
	}

	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public get(String position) {
		/*getter*/
	}

	public set(String position, Piece piece) {
		/*setter*/
	}
}
